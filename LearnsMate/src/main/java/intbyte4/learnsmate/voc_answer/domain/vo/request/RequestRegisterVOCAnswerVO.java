package intbyte4.learnsmate.voc_answer.domain.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class RequestRegisterVOCAnswerVO {
    @JsonProperty("voc_answer_code")
    private Long vocAnswerCode;
    @JsonProperty("voc_answer_content")
    private String vocAnswerContent;
    @JsonProperty("voc_code")
    private Long vocCode;
    @JsonProperty("admin_code")
    private Long adminCode;
}
