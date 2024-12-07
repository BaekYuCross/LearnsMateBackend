package intbyte4.learnsmate.contract_status.domain.entity;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity(name = "contract_status")
@Table(name = "contract")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class ContractStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_status_code", nullable = false)
    private Long contractStatusCode;

    @Column(name = "approval_status", nullable = false)
    private Integer approvalStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "lecture_code", nullable = false)
    private Lecture lecture;

    @ManyToOne
    @JoinColumn(name = "admin_code", nullable = false)
    private Admin admin;
}
