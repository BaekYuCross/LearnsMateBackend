package intbyte4.learnsmate.member.domain.dto;

import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberVOCDTO {
    List<VOCDTO> unansweredVOCByMember;
    List<VOCDTO> answeredVOCByMember;
}
