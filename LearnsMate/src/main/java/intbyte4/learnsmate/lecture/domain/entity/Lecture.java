package intbyte4.learnsmate.lecture.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "lecture")
@Table(name = "lecture")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class Lecture {
    @Id
    @Column(name = "lecture_code")
    private Long lectureCode;

    @Column(name = "lecture_title")
    private String lectureTitle;

    @Column(name = "lecture_category")
    private Integer lectureCategory;

    @Column(name = "lecture_confirm_status")
    private Boolean lectureConfirmStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "lecture_image")
    private String lectureImage;

    @Column(name = "lecture_price")
    private Integer lecturePrice;

    @Column(name = "tutor_code")
    private Long tutorCode;

    @Column(name = "lecture_status")
    private Boolean lectureStatus;

    @Column(name = "lecture_click_count")
    private Integer lectureClickCount;

    @Column(name = "lecture_level")
    private Integer lectureLevel;
}
