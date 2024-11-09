package intbyte4.learnsmate.videobylecture.domain.dto;

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
    private long videoCount;
}
