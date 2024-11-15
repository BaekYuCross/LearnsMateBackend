package intbyte4.learnsmate.lecture.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MonthlyLectureCountDTO {
    private String date;
    private int lectureCount;
}
