package intbyte4.learnsmate.lecture_video_by_student.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureVideoProgressDTO {
    private Long lectureCode;  // 강의 코드
    private String lectureTitle;  // 강의제목
    private long totalVideos;  // 총 동영상 수
    private long completedVideos;  // 완료된 동영상 수
    private double progress;  // 진척도 (완료된 동영상 / 총 동영상)
}
