package intbyte4.learnsmate.lecture.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

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
    @Column(name = "lecture_code", nullable = false)
    private String lectureCode;

    @Column(name = "lecture_title", nullable = false)
    private String lectureTitle;

    @Column(name = "lecture_confirm_status", nullable = false)
    private Boolean lectureConfirmStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "lecture_image", nullable = false , length = 3000)
    private String lectureImage;

    @Column(name = "lecture_price", nullable = false)
    private Integer lecturePrice;

    @ManyToOne
    @JoinColumn(name = "tutor_code", nullable = false)
    @Where(clause = "member_type = 'TUTOR'")
    private Member tutor;

    @Column(name = "lecture_status", nullable = false)
    private Boolean lectureStatus;

    @Column(name = "lecture_click_count", nullable = false)
    private Integer lectureClickCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "lecture_level", nullable = false)
    private LectureLevelEnum lectureLevel;

    public void toUpdate(LectureDTO lectureDTO) {
        this.lectureTitle = lectureDTO.getLectureTitle();
        this.lectureConfirmStatus = lectureDTO.getLectureConfirmStatus();
        this.updatedAt = LocalDateTime.now();
        this.lectureImage = lectureDTO.getLectureImage();
        this.lecturePrice = lectureDTO.getLecturePrice();
        this.lectureStatus = lectureDTO.getLectureStatus();
        this.lectureClickCount = lectureDTO.getLectureClickCount();
        this.lectureLevel = LectureLevelEnum.valueOf(lectureDTO.getLectureLevel());
    }

    public void toDelete(){
        this.lectureStatus = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void toAcceptConfirmStatus(){
        this.lectureConfirmStatus = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void setLectureCode(String lectureCode) {
        this.lectureCode = lectureCode;
    }
}
