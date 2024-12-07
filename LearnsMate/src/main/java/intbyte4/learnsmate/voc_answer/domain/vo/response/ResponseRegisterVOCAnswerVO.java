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
public class ResponseRegisterVOCAnswerVO {
    @JsonProperty("voc_answer_code")
    private Long vocAnswerCode;
    @JsonProperty("voc_answer_content")
    private String vocAnswerContent;
    @JsonProperty("admin_code")
    private Long adminCode;
}
