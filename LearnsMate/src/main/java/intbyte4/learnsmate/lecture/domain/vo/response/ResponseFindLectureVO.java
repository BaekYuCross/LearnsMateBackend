package intbyte4.learnsmate.lecture.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseFindLectureVO {
    private String lectureCode;
    private String lectureTitle;
    private Long tutorCode;
    private String tutorName;
    private String lectureCategoryName;
    private String lectureLevel;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    private Integer lecturePrice;
    private Boolean lectureConfirmStatus;
    private Boolean lectureStatus;
}
