package intbyte4.learnsmate.voc_answer.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class VocAnswerDTO {
    private Long vocAnswerCode;
    private String vocAnswerContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long vocCode;
    private Long adminCode;
}
