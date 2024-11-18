package intbyte4.learnsmate.voc.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseFindVOCVO {
    @JsonProperty("voc_code")
    private Long vocCode;
    @JsonProperty("voc_content")
    private String vocContent;
    @JsonProperty("voc_category_name")
    private String vocCategoryName;
    @JsonProperty("member_type")
    private String memberType;
    @JsonProperty("member_name")
    private String memberName;
    @JsonProperty("member_code")
    private Long memberCode;
    @JsonProperty("admin_name")
    private String adminName;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("voc_answer_status")
    private Boolean vocAnswerStatus;
    @JsonProperty("voc_answer_satisfaction")
    private String vocAnswerSatisfaction;
    @JsonProperty("voc_analysis")
    private String vocAnalysis;
}
