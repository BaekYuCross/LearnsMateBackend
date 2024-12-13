package intbyte4.learnsmate.voc.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class VOCClientDTO {
    private String vocCode;
    private String vocContent;
    private Boolean vocAnswerStatus;
    private String vocAnswerSatisfaction;
    private LocalDateTime createdAt;
    private int vocCategoryCode;
    private String vocCategoryName;
    private Long memberCode;
    private String memberName;

    private Long vocAnswerCode;
    private String vocAnswerContent;
    private LocalDateTime vocAnswerCreatedAt;
    private LocalDateTime vocAnswerUpdatedAt;
    private Long adminCode;
    private String adminName;
}
