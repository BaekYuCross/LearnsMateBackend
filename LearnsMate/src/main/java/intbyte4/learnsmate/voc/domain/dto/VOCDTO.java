package intbyte4.learnsmate.voc.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class VOCDTO {
    private String vocCode;
    private String vocContent;
    private Boolean vocAnswerStatus;
    private String vocAnswerSatisfaction;
    private LocalDateTime createdAt;
    private int vocCategoryCode;
    private Long memberCode;
}
