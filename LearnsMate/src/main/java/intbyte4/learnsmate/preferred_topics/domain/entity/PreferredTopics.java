package intbyte4.learnsmate.preferred_topics.domain.entity;

import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategory;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "preferredTopics")
@Table(name = "preferred_topics")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PreferredTopics {

    @Id
    @Column(name = "preferred_topic_code", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long preferredTopicCode;

    @ManyToOne
    @JoinColumn(name = "student_code", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "lecture_category_code", nullable = false)
    private LectureCategory lectureCategory;
}
