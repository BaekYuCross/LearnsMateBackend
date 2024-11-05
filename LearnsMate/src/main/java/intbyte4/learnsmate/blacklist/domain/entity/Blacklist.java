package intbyte4.learnsmate.blacklist.domain.entity;

import intbyte4.learnsmate.member.domain.MemberType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "Blacklist")
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

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    //    @ManyToOne
    //    @JoinColumn
    @Column(name = "member_code", nullable = false)
    private Long memberCode;

    //    @ManyToOne
    //    @JoinColumn
    @Column(name = "report_code", nullable = false)
    private Long reportCode;

    //    @ManyToOne
    //    @JoinColumn
    @Column(name = "admin_code", nullable = false)
    private Long adminCode;
}
