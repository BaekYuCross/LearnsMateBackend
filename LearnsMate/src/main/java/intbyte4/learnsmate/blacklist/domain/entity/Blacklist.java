package intbyte4.learnsmate.blacklist.domain.entity;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.report.domain.entity.Report;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "blacklist")
@Table(name = "blacklist")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Blacklist {

    @Id
    @Column(name = "black_code", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long blackCode;

    @Column(name = "black_reason", nullable = false)
    private String blackReason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

//    @Column(name = "updated_at", nullable = false)
//    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "member_code", nullable = false)
    private Member member;

//    @ManyToOne
//    @JoinColumn(name = "report_code", nullable = false)
//    private Report report;

    @ManyToOne
    @JoinColumn(name = "admin_code", nullable = false)
    private Admin admin;
}
