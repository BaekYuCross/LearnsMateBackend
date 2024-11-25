package intbyte4.learnsmate.lecture.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MonthlyLectureFilterDTO {
    private Integer startYear;
    private Integer startMonth;
    private Integer endYear;
    private Integer endMonth;
}