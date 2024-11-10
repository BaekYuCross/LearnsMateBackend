package intbyte4.learnsmate.lecture.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TutorLectureVideoCountDTO {
    private Long lectureCode;
    private String lectureTitle;
    private long videoCount;
}
