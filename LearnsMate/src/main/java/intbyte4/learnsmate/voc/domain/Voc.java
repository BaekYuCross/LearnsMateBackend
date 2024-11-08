package intbyte4.learnsmate.voc.domain;

import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.voc_category.domain.VocCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "Voc")
@Table(name = "voc")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Voc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voc_code", nullable = false, unique = true)
    private Long vocCode;

    @Column(name = "voc_content", nullable = false)
    private String vocContent;

    @Column(name = "voc_answer_status", nullable = false)
    private int vocAnswerStatus;

    @Column(name = "voc_answer_satisfaction")
    private String vocAnswerSatisfaction;

    @Column(name = "voc_analysis", nullable = false)
    private String vocAnalysis;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "voc_category_code", nullable = false)
    private VocCategory vocCategory;

    @ManyToOne
    @JoinColumn(name = "member_code", nullable = false)
    private Member member;
}
