package intbyte4.learnsmate.member.repository;

import intbyte4.learnsmate.member.domain.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> searchBy(Member member,
                                 LocalDateTime birthStartDate, LocalDateTime birthEndDate,
                                 LocalDateTime createdStartDate, LocalDateTime createdEndDate,
                                 LocalDateTime updatedStartDate, LocalDateTime updatedEndDate);
}
