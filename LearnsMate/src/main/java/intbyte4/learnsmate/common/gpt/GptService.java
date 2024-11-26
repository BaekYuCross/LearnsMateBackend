package intbyte4.learnsmate.common.gpt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GptService {

    private final RestTemplate restTemplate;

    @Value("${openai.secret-key}")
    private String apiSecretKey;

    public String analyzeVocContents(String vocContents) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiSecretKey);

        Map<String, Object> messages = Map.of(
                "messages", List.of(
                        Map.of("role", "system", "content", "VOC 분석을 전문으로 하는 AI 어시스턴트입니다."),
                        Map.of("role", "user", "content", """
                                - VOC 내용을 분석하여 상위 3개 키워드를 반드시 JSON 형식으로 반환해주세요.
                                - 각 키워드에 대해 추천 답안을 포함해야 합니다.
                                - 출력 형식:
                                {
                                  "insights": [
                                    {"keyword": "키워드1", "count": N},
                                    {"keyword": "키워드2", "count": N}
                                  ],
                                  "recommendations": [
                                    {"keyword": "키워드1", "recommendation": "추천 답안1"},
                                    {"keyword": "키워드2", "recommendation": "추천 답안2"}
                                  ]
                                }
                                VOC 내용:
                                %s
                                """.formatted(vocContents))
                )
        );

        Map<String, Object> request = Map.of(
                "model", "gpt-4",
                "messages", messages.get("messages"),
                "max_tokens", 1000,  // 토큰 초과 방지
                "temperature", 0.7
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions", entity, String.class
            );
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error analyzing VOC contents: " + e.getMessage(), e);
        }
    }
}
