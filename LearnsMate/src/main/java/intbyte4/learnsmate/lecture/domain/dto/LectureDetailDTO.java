package intbyte4.learnsmate.lecture.domain.dto;

import intbyte4.learnsmate.lecture.domain.entity.LectureLevelEnum;
import intbyte4.learnsmate.video_by_lecture.domain.dto.VideoByLectureDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LectureDetailDTO {
    private Long lectureCode; // 강의코드
    private String lectureTitle; // 강의명
    private Boolean lectureConfirmStatus; // 강의 계약 상태
    private LocalDateTime createdAt; // 강의 생성일
    private String lectureImage; // 강의 이미지
    private Integer lecturePrice; // 금액
    private Long tutorCode; // 강사 코드
    private String tutorName; // 강사 명
    private Boolean lectureStatus; // 강의 상태
    private String lectureCategory; // 강의 카테고리
    private Integer lectureClickCount; // 강의 조회수
    private LectureLevelEnum lectureLevel; // 강의 난이도
    private int totalStudents; // 누적 수강생
    private int totalRevenue; // 누적 매출액
    private List<VideoByLectureDTO> lectureVideos;  // 강의 동영상 정보
}
