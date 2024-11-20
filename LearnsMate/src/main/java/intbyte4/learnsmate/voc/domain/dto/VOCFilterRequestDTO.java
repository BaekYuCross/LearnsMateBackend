package intbyte4.learnsmate.voc.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class VOCFilterRequestDTO {
    private String vocCode;
    private String vocContent;
    private Integer vocCategoryCode;
    private String memberType;
    private Boolean vocAnswerStatus;
    private String vocAnswerSatisfaction;
    private LocalDateTime startCreateDate;
    private LocalDateTime startEndDate;
}
