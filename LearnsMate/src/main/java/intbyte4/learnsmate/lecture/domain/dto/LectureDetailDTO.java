package intbyte4.learnsmate.lecture.domain.dto;

import intbyte4.learnsmate.video_by_lecture.domain.dto.VideoByLectureDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class LectureDetailDTO {
    private String lectureCode;
    private String lectureTitle;
    private Boolean lectureConfirmStatus;
    private LocalDateTime createdAt;
    private String lectureImage;
    private Integer lecturePrice;
    private Long tutorCode;
    private String tutorName;
    private Boolean lectureStatus;
    private String lectureCategory;
    private Integer lectureClickCount;
    private String lectureLevel;
    private int totalStudents;
    private int totalRevenue;
    private List<VideoByLectureDTO> lectureVideos;
}
