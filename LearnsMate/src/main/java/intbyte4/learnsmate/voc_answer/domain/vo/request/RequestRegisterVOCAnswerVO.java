package intbyte4.learnsmate.voc_answer.domain.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RequestRegisterVOCAnswerVO {
    @JsonProperty("voc_answer_content")
    private String vocAnswerContent;
    @JsonProperty("voc_code")
    private String vocCode;
    @JsonProperty("admin_code")
    private Long adminCode;
}
