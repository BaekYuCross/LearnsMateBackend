package intbyte4.learnsmate.video_by_lecture.domain.entity;

import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import jakarta.persistence.*;
import lombok.*;


@Entity(name = "video_by_lecture")
@Table(name = "video_by_lecture")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class VideoByLecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_code", nullable = false)
    private Long videoCode;

    @Column(name = "video_link", nullable = false)
    private String videoLink;

    @Column(name = "video_title", nullable = false)
    private String videoTitle;

    @ManyToOne
    @JoinColumn(name = "lecture_code", nullable = false)
    private Lecture lecture;
}
