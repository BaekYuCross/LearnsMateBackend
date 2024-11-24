package intbyte4.learnsmate.member.domain.dto;

import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture_video_by_student.domain.dto.LectureVideoProgressDTO;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FindSingleStudentDTO {
    MemberDTO memberDTO;

    List<LectureVideoProgressDTO> LectureVideoProgressDTOList;

    List<IssueCouponDTO> unusedCouponsList;
    List<IssueCouponDTO> usedCouponsList;

    List<VOCDTO> unansweredVOCByMemberList;
    List<VOCDTO> answeredVOCByMemberList;

    List<LectureDTO> recommendedLectureList;
}
