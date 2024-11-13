package intbyte4.learnsmate.member.domain.dto;

import intbyte4.learnsmate.video_by_lecture.domain.dto.CountVideoByLectureDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FindSingleTutorDTO {

    MemberDTO memberDTO;
    List<CountVideoByLectureDTO> tutorLectureDetailList;
}
