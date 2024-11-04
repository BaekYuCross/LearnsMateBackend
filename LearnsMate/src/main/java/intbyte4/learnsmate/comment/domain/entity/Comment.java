package intbyte4.learnsmate.comment.domain.entity;

import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "comments")
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_code", nullable = false)
    private Long commentCode;

    @Column(name = "comment_content", nullable = false, length = 3000)
    private String commentContent;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "member_code", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "lecture_code", nullable = false)
    private Lecture lecture;

}
