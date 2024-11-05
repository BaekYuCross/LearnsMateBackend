package intbyte4.learnsmate.report.domain.entity;

import intbyte4.learnsmate.comment.domain.entity.Comment;
import intbyte4.learnsmate.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "report")
@Table(name = "report")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Report {

    @Id
    @Column(name = "report_code", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reportCode;

    @Column(name = "report_reason", nullable = false)
    private String reportReason;

    @Column(name = "report_date", nullable = false)
    private LocalDateTime reportDate;

    @ManyToOne
    @JoinColumn(name = "comment_code", nullable = false)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "report_member_code", nullable = false)
    private Member reportMember;

    @ManyToOne
    @JoinColumn(name = "reported_member_code", nullable = false)
    private Member reportedMember;
}
