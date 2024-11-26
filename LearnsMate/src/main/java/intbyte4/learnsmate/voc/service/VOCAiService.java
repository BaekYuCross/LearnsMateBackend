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
        log.info("Fetching weekly analysis for the week starting at {}", weekStartDate);

        List<VOCAiAnswer> results = vocAiAnswerRepository.findAllByAnalysisDate(weekStartDate);
        if (results.isEmpty()) {
            log.info("No analysis data found for the week starting at {}", weekStartDate);
        }
        return results;
    }

    public LocalDate getCurrentWeekMonday() {
        return LocalDate.now().with(DayOfWeek.MONDAY);
    }

    @Transactional
    public void analyzeVocForLastWeek() {
        LocalDateTime startOfLastWeek = LocalDate.now().minusWeeks(1).with(DayOfWeek.MONDAY).atTime(9, 0);
        LocalDateTime endOfLastWeek = LocalDate.now().with(DayOfWeek.MONDAY).atTime(8, 59, 59);

        log.info("Analyzing VOC from {} to {}", startOfLastWeek, endOfLastWeek);

        List<Object[]> keywordFrequencyData = vocRepository.findKeywordFrequencyBetweenDates(startOfLastWeek, endOfLastWeek);

        if (keywordFrequencyData.isEmpty()) {
            log.info("No significant keywords found.");
            return;
        }

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
            log.info("No significant keywords found.");
            return;
        }

        StringBuilder vocContents = new StringBuilder("VOC 분석 데이터:\n");
        StringBuilder recommendations = new StringBuilder("추천 답안:\n");

        for (Map.Entry<String, Integer> entry : topKeywords) {
            String keyword = entry.getKey();
            int count = entry.getValue();

            vocContents.append(keyword).append(": ").append(count).append("건\n");

            List<VOC> relatedVocs = vocRepository.findVocByKeywordAndSatisfaction(startOfLastWeek, endOfLastWeek, keyword);
            for (VOC voc : relatedVocs) {
                Optional<VOCAnswer> vocAnswerOptional = vocAnswerRepository.findByVoc_VocCode(voc.getVocCode());
                vocAnswerOptional.ifPresent(vocAnswer ->
                        recommendations.append("- ").append(vocAnswer.getVocAnswerContent()).append("\n")
                );
            }
        }

        try {
            String response = gptService.analyzeVocContents(vocContents.toString());
            processGptResponse(response);
        } catch (Exception e) {
            log.error("Error analyzing VOC contents: {}", e.getMessage(), e);
        }
    }

    private void processGptResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode fullResponseJson = objectMapper.readTree(response);

            JsonNode choices = fullResponseJson.get("choices");
            if (choices == null || !choices.isArray() || choices.isEmpty()) {
                log.error("Invalid GPT response format. Missing choices array.");
                return;
            }

            String content = choices.get(0).get("message").get("content").asText();
            log.info("Extracted content from GPT response: {}", content);

            JsonNode contentJson = objectMapper.readTree(content);

            // insights와 recommendations 추출
            JsonNode insights = contentJson.get("insights");
            JsonNode recommendations = contentJson.get("recommendations");

            if (insights == null || recommendations == null) {
                log.error("Invalid GPT response format. Insights or recommendations missing.");
                return;
            }

            if (!insights.isArray() || !recommendations.isArray()) {
                log.error("Invalid GPT response format. Insights or recommendations are not arrays.");
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
                            .createdAt(LocalDateTime.now())
                            .build();

                    vocAiAnswerRepository.save(vocAiAnswer);
                    processedKeywords.add(keyword);
                }
            }
        } catch (JsonProcessingException e) {
            log.error("Error parsing GPT response as JSON: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while processing GPT response: {}", e.getMessage(), e);
        }
    }
}
