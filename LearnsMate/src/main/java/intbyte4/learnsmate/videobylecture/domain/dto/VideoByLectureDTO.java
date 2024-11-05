package intbyte4.learnsmate.videobylecture.domain.dto;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VideoByLectureDTO {

    private Long videoCode;
    private String videoLink;
    private String videoTitle;
    private Long lectureCode;

}
