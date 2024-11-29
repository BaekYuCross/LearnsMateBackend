package intbyte4.learnsmate.coupon.domain.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LectureRequestVO {

    @JsonProperty("lecture_code")
    private String lectureCode;

    @JsonProperty("lecture_title")
    private String lectureTitle;

    @JsonProperty("lecture_confirm_status")
    private Boolean lectureConfirmStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
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
    private String lectureLevel;
}
