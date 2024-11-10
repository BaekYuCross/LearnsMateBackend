package intbyte4.learnsmate.video_by_lecture.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountVideoByLectureDTO {
    private Long lectureCode;
    private String lectureTitle;
    private long videoCount;

}
