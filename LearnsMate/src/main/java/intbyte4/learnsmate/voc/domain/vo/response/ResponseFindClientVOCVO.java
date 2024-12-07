package intbyte4.learnsmate.voc.domain.vo.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseFindClientVOCVO {
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
