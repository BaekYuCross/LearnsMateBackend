package intbyte4.learnsmate.comment.domain.entity;

import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.member.domain.entity.Member;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_code")
    private Long commentCode;

    @Column(name = "comment_content", length = 3000)
    private String commentContent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_code")
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lecture_code")
    private Lecture lecture;

}
