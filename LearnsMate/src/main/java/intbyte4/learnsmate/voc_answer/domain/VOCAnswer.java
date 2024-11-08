package intbyte4.learnsmate.voc_answer.domain;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.voc.domain.Voc;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "VocAnswer")
@Table(name = "voc_answer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class VOCAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vos_answer_code", nullable = false, unique = true)
    private Long vocAnswerCode;

    @Column(name = "vos_answer_content", nullable = false)
    private String vocAnswerContent;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "voc_code", nullable = false)
    private Voc voc;

    @ManyToOne
    @JoinColumn(name = "admin_code", nullable = false)
    private Admin admin;
}
