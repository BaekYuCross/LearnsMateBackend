package intbyte4.learnsmate.video_by_lecture.domain.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountVideoByLectureDTO {
    private String lectureCode;
    private String lectureTitle;
    private long videoCount;
    private long totalStudents;

}