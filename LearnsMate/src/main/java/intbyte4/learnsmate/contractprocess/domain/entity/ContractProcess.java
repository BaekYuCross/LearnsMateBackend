package intbyte4.learnsmate.contractprocess.domain.entity;

import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity(name = "contract_process")
@Table(name = "contract_process")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class ContractProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_process_code", nullable = false)
    private Long contractProcessCode;

    @Column(name = "approval_process", nullable = false)
    private Integer approvalProcess;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "lecture_code", nullable = false)
    private Lecture lecture;

//    @ManyToOne
//    @JoinColumn(name = "admin_code", nullable = false)
//    private Admin admin;
}
