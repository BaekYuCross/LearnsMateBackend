package intbyte4.learnsmate.video_by_lecture.domain.dto;


import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VideoByLectureDTO {

    private Long videoCode;
    private String videoLink;
    private String videoTitle;
    private Long lectureCode;

}
