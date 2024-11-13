package intbyte4.learnsmate.member.repository;

import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.domain.dto.MemberFilterRequestDTO;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> searchBy(MemberFilterRequestDTO request);
}
