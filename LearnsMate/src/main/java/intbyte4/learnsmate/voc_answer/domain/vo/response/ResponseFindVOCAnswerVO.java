package intbyte4.learnsmate.voc_answer.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseFindVOCAnswerVO {
    @JsonProperty("voc_answer_code")
    private Long vocAnswerCode;
    @JsonProperty("voc_answer_content")
    private String vocAnswerContent;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    @JsonProperty("voc_code")
    private String vocCode;
    @JsonProperty("admin_code")
    private Long adminCode;
}
