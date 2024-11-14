package intbyte4.learnsmate.lecture.domain.dto;

import intbyte4.learnsmate.lecture.enums.LectureLevelEnum;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LectureFilterDTO {
    private Long lectureCode; // 강의 코드
    private String lectureTitle; // 강의명
    private Long tutorCode; // 강사 코드
    private String tutorName; // 강사명
    private String lectureCategory; // 강의 카테고리
    private LectureLevelEnum lectureLevel; // 강의 난이도
    private Boolean lectureConfirmStatus; // 강의 계약 상태
    private Boolean lectureStatus; // 강의 상태
    private Integer price; // 금액
    private LocalDateTime createdAt; // 강의 생성일
}
