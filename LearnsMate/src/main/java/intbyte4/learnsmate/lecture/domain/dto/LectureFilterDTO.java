package intbyte4.learnsmate.lecture.domain.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class LectureFilterDTO {
    private String lectureCode; // 강의 코드
    private String lectureTitle; // 강의명
    private Long tutorCode; // 강사 코드
    private String tutorName; // 강사명
    private String lectureCategoryName;
    private String lectureLevel;
    private Boolean lectureConfirmStatus; // 강의 계약 상태
    private Boolean lectureStatus; // 강의 상태
    private Integer minPrice; // 금액
    private Integer maxPrice; // 금액
    private LocalDate startCreatedAt; // 강의 생성일
    private LocalDate endCreatedAt; // 강의 생성일
}

