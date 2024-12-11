package intbyte4.learnsmate.voc.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VOCFilterRequestDTO {
    @JsonProperty("voc_code")
    private String vocCode;

    @JsonProperty("voc_content")
    private String vocContent;

    @JsonProperty("voc_category_code")
    private Integer vocCategoryCode;

    @JsonProperty("member_type")
    private String memberType;

    @JsonProperty("voc_answer_status")
    private Boolean vocAnswerStatus;

    @JsonProperty("voc_answer_satisfaction")
    private String vocAnswerSatisfaction;

    @JsonProperty("start_create_date")
    private LocalDateTime startCreateDate;

    @JsonProperty("end_create_date")
    private LocalDateTime startEndDate;
}
