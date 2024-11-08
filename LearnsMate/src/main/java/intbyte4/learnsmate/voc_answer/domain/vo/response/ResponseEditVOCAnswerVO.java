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
public class ResponseEditVOCAnswerVO {
    @JsonProperty("vos_answer_content")
    private String vocAnswerContent;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
