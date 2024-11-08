package intbyte4.learnsmate.voc.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Builder
public class ResponseFindVocVO {
    @JsonProperty("voc_code")
    private Long vocCode;
    @JsonProperty("voc_content")
    private String vocContent;
    @JsonProperty("voc_answer_status")
    private int vocAnswerStatus;
    @JsonProperty("voc_answer_satisfaction")
    private String vocAnswerSatisfaction;
    @JsonProperty("voc_analysis")
    private String vocAnalysis;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("voc_category_code")
    private int vocCategoryCode;
    @JsonProperty("member_code")
    private Long memberCode;
}
