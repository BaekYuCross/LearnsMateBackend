package intbyte4.learnsmate.lecture.domain.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class LectureFilterDTO {
    private String lectureCode;
    private String lectureTitle;
    private Long tutorCode;
    private String tutorName;
    private String lectureCategoryName;
    private String lectureLevel;
    private Boolean lectureConfirmStatus;
    private Boolean lectureStatus;
    private Integer minLecturePrice;
    private Integer maxLecturePrice;
    private LocalDate startCreatedAt;
    private LocalDate endCreatedAt;
    private List<String> selectedColumns;
}

