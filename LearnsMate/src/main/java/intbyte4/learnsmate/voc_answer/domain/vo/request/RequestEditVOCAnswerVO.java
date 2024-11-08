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
public class RequestEditVOCAnswerVO {
    @JsonProperty("voc_answer_content")
    private String vocAnswerContent;
}
