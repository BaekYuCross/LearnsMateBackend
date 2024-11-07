package intbyte4.learnsmate.voc.domain.dto;

import intbyte4.learnsmate.member.domain.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class VocDTO {

    private Long vocCode;
    private String vocContent;
    private int vocAnswerStatus;
    private String vocAnswerSatisfaction;
    private String vocAnalysis;
    private LocalDateTime createdAt;
    private int vocCategoryCode;
    private Long memberCode;
}
