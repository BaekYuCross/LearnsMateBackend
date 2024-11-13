package intbyte4.learnsmate.member.domain.vo.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.video_by_lecture.domain.dto.CountVideoByLectureDTO;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseFindTutorDetailVO {

    MemberDTO memberDTO;
    List<CountVideoByLectureDTO> tutorLectureDetailList;
}
