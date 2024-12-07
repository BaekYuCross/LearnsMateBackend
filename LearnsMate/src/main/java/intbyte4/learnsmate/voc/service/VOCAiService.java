package intbyte4.learnsmate.voc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import intbyte4.learnsmate.common.gpt.GptService;
import intbyte4.learnsmate.voc.domain.VOC;
import intbyte4.learnsmate.voc.domain.VOCAiAnswer;
import intbyte4.learnsmate.voc.repository.VOCAiAnswerRepository;
import intbyte4.learnsmate.voc.repository.VOCRepository;
import intbyte4.learnsmate.voc_answer.domain.VOCAnswer;
import intbyte4.learnsmate.voc_answer.repository.VOCAnswerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VOCAiService {

    private final VOCRepository vocRepository;
    private final VOCAnswerRepository vocAnswerRepository;
    private final VOCAiAnswerRepository vocAiAnswerRepository;
    private final GptService gptService;

    @Transactional
    public List<VOCAiAnswer> getWeeklyAnalysis(LocalDate weekStartDate) {
        log.info("주차 분석 데이터 조회 시작: 기준 주차 {}", weekStartDate);

        List<VOCAiAnswer> results = vocAiAnswerRepository.findAllByAnalysisDate(weekStartDate);
        if (results.isEmpty()) {
            log.info("해당 주차({})에 저장된 분석 데이터가 없습니다.", weekStartDate);
        } else {
            log.info("해당 주차({})에 {}개의 분석 데이터를 조회했습니다.", weekStartDate, results.size());
        }
        return results;
    }

    public LocalDate getCurrentWeekMonday() {
        return LocalDate.now().with(DayOfWeek.MONDAY);
    }

    @Transactional
    public List<VOCAiAnswer> getAnalysisByDate(LocalDate targetDate) {
        LocalDate weekMonday = targetDate.with(DayOfWeek.MONDAY); // 기준 날짜의 주차 월요일 계산
        log.info("기준 날짜: {}를 포함하는 주차 데이터 조회를 시작합니다. 기준 주차 월요일: {}", targetDate, weekMonday);

        List<VOCAiAnswer> results = vocAiAnswerRepository.findAllByAnalysisDate(weekMonday);
        if (results.isEmpty()) {
            log.info("해당 주차({})에 저장된 VOC 분석 데이터가 없습니다.", weekMonday);
        } else {
            log.info("해당 주차({})에 {}개의 VOC 분석 데이터를 조회했습니다.", weekMonday, results.size());
        }
        return results;
    }

    @Transactional
    public void analyzeVocForLastWeek() {
        LocalDateTime startOfLastWeek = LocalDate.now().minusWeeks(1).with(DayOfWeek.MONDAY).atTime(9, 0);
        LocalDateTime endOfLastWeek = LocalDate.now().with(DayOfWeek.MONDAY).atTime(8, 59, 59);

        log.info("지난 주 VOC 분석 시작: {} ~ {}", startOfLastWeek, endOfLastWeek);

        List<Object[]> keywordFrequencyData = vocRepository.findKeywordFrequencyBetweenDates(startOfLastWeek, endOfLastWeek);

        if (keywordFrequencyData.isEmpty()) {
            log.info("지난 주({} ~ {}) 동안 발견된 주요 키워드가 없습니다.", startOfLastWeek, endOfLastWeek);
            return;
        }

        log.info("지난 주({} ~ {}) 동안 발견된 키워드 개수: {}", startOfLastWeek, endOfLastWeek, keywordFrequencyData.size());

        Map<String, Integer> keywordFrequency = keywordFrequencyData.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> ((Number) row[1]).intValue()
                ));

        List<Map.Entry<String, Integer>> topKeywords = keywordFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .toList();

        if (topKeywords.isEmpty()) {
            log.info("지난 주({} ~ {}) 동안 상위 키워드가 없습니다.", startOfLastWeek, endOfLastWeek);
            return;
        }

        log.info("지난 주 상위 키워드: {}", topKeywords);

        StringBuilder vocContents = new StringBuilder("VOC 분석 데이터:\n");
        StringBuilder recommendations = new StringBuilder("추천 답안:\n");

        for (Map.Entry<String, Integer> entry : topKeywords) {
            String keyword = entry.getKey();
            int count = entry.getValue();
            log.info("키워드 '{}'에 대한 VOC {}건 분석 중...", keyword, count);

            vocContents.append(keyword).append(": ").append(count).append("건\n");

            List<VOC> relatedVocs = vocRepository.findVocByKeywordAndSatisfaction(startOfLastWeek, endOfLastWeek, keyword);
            for (VOC voc : relatedVocs) {
                Optional<VOCAnswer> vocAnswerOptional = vocAnswerRepository.findByVoc_VocCode(voc.getVocCode());
                vocAnswerOptional.ifPresent(vocAnswer -> {
                    recommendations.append("- ").append(vocAnswer.getVocAnswerContent()).append("\n");
                });
            }
        }

        try {
            String response = gptService.analyzeVocContents(vocContents.toString());
            processGptResponse(response);
        } catch (Exception e) {
            log.error("GPT로 VOC 분석 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    private void processGptResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode fullResponseJson = objectMapper.readTree(response);

            JsonNode choices = fullResponseJson.get("choices");
            if (choices == null || !choices.isArray() || choices.isEmpty()) {
                log.error("GPT 응답 형식 오류: 'choices' 배열이 없습니다.");
                return;
            }

            String content = choices.get(0).get("message").get("content").asText();
            log.info("GPT 응답에서 추출된 내용: {}", content);

            JsonNode contentJson = objectMapper.readTree(content);

            JsonNode insights = contentJson.get("insights");
            JsonNode recommendations = contentJson.get("recommendations");

            if (insights == null || recommendations == null) {
                log.error("GPT 응답 형식 오류: 'insights' 또는 'recommendations' 항목이 없습니다.");
                return;
            }

            if (!insights.isArray() || !recommendations.isArray()) {
                log.error("GPT 응답 형식 오류: 'insights' 또는 'recommendations'가 배열이 아닙니다.");
                return;
            }

            Set<String> processedKeywords = new HashSet<>();

            for (JsonNode insight : insights) {
                String keyword = insight.get("keyword").asText();
                int count = insight.get("count").asInt();

                if (processedKeywords.contains(keyword)) continue;

                String recommendation = recommendations.findValuesAsText("recommendation").stream()
                        .filter(r -> recommendations.findValue("keyword").asText().equals(keyword))
                        .findFirst()
                        .orElse("추천 답안 없음");

                if (!vocAiAnswerRepository.existsByAnalysisDateAndKeyword(LocalDate.now(), keyword)) {
                    VOCAiAnswer vocAiAnswer = VOCAiAnswer.builder()
                            .analysisDate(LocalDate.now())
                            .keyword(keyword)
                            .keywordCount(count)
                            .recommendation(recommendation)
                            .createdAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                            .build();

                    vocAiAnswerRepository.save(vocAiAnswer);
                    log.info("키워드 '{}' 저장 완료: {}건", keyword, count);
                    processedKeywords.add(keyword);
                }
            }
        } catch (JsonProcessingException e) {
            log.error("GPT 응답을 JSON으로 처리 중 오류 발생: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("GPT 응답 처리 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
        }
    }
}