package intbyte4.learnsmate.lecture.domain.vo.response;

import intbyte4.learnsmate.lecture.enums.LectureLevelEnum;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseLectureFilterRequestVO {
    private Long lectureCode; // 강의 코드
    private String lectureTitle; // 강의명
    private Long tutorCode; // 강사 코드
    private String tutorName; // 강사 명
    private String lectureCategory; // 강의 카테고리
    private LectureLevelEnum lectureLevel; // 강의 난이도
    private Boolean lectureConfirmStatus; // 강의 계약 상태
    private Boolean lectureStatus; // 강의 상태
    private Integer minPrice; // 최소 금액
    private Integer maxPrice; // 최대 금액
    private LocalDateTime startCreatedAt; // 강의 생성일 시작
    private LocalDateTime endCreatedAt; // 강의 생성일 끝
}
