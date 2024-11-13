package intbyte4.learnsmate.member.domain.dto;

import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FindSingleMemberDTO {

    MemberDTO memberDTO;

    List<LectureDTO> lectureDTOList;

    List<IssueCouponDTO> unusedCouponsList;
    List<IssueCouponDTO> usedCouponsList;

    List<VOCDTO> unansweredVOCByMemberList;
    List<VOCDTO> answeredVOCByMemberList;
}
