package intbyte4.learnsmate.report.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "Report")
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

    //    @ManyToOne
    //    @JoinColumn
    @Column(name = "comment_code", nullable = false)
    private Long commentCode;

    //    @ManyToOne
    //    @JoinColumn
    @Column(name = "report_member_code", nullable = false)
    private Long reportMemberCode;

    //    @ManyToOne
    //    @JoinColumn
    @Column(name = "reported_member_code", nullable = false)
    private Long reportedMemberCode;
}
