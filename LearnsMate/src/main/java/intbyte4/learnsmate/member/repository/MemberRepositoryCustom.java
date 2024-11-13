package intbyte4.learnsmate.member.repository;

import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.domain.vo.MemberFilterRequestVO;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> searchBy(MemberFilterRequestVO request);
}
