package intbyte4.learnsmate.member.repository;

import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.domain.dto.MemberFilterRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberRepositoryCustom {
    Page<Member> searchBy(MemberFilterRequestDTO request, Pageable pageable);
}
