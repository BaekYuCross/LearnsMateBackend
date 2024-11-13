package intbyte4.learnsmate.member.domain.vo.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.lecture_video_by_student.domain.dto.LectureVideoProgressDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseFindMemberDetailVO {

    MemberDTO memberDTO;

    List<LectureVideoProgressDTO> LectureVideoProgressDTOList;

    List<IssueCouponDTO> unusedCouponsList;
    List<IssueCouponDTO> usedCouponsList;

    List<VOCDTO> unansweredVOCByMemberList;
    List<VOCDTO> answeredVOCByMemberList;
}
