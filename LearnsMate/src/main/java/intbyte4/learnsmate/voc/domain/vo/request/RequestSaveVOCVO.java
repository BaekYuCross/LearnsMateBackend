package intbyte4.learnsmate.voc.domain.vo.request;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RequestSaveVOCVO {
    private String vocCode;
    private String vocContent;
    private Boolean vocAnswerStatus;
    private String vocAnswerSatisfaction;
    private LocalDateTime createdAt;
    private int vocCategoryCode;
    private Long memberCode;
}
