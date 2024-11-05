package intbyte4.learnsmate.lecture.domain.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LectureDTO {
    @JsonProperty("lecture_code")
    private Long lectureCode;

    @JsonProperty("lecture_title")
    private String lectureTitle;

    @JsonProperty("lecture_category")
    private Integer lectureCategory;

    @JsonProperty("lecture_confirm_status")
    private Boolean lectureConfirmStatus;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("lecture_image")
    private String lectureImage;

    @JsonProperty("lecture_price")
    private Integer lecturePrice;

    @JsonProperty("tutor_code")
    private Long tutorCode;

    @JsonProperty("lecture_status")
    private Boolean lectureStatus;

    @JsonProperty("lecture_click_count")
    private Integer lectureClickCount;

    @JsonProperty("lecture_level")
    private Integer lectureLevel;

    public Lecture toLecture() {
        return Lecture.builder()
                .lectureCode(this.lectureCode)
                .lectureTitle(this.lectureTitle)
                .lectureCategory(this.lectureCategory)
                .lectureConfirmStatus(this.lectureConfirmStatus)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .lectureImage(this.lectureImage)
                .lecturePrice(this.lecturePrice)
                .lectureStatus(this.lectureStatus)
                .lectureClickCount(this.lectureClickCount)
                .lectureLevel(this.lectureLevel)
                .build();
    }
}
