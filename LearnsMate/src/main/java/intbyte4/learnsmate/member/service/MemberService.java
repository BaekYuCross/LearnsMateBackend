package intbyte4.learnsmate.member.service;

import intbyte4.learnsmate.common.mapper.MemberMapper;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Autowired
    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    public void saveMember(MemberDTO memberDTO) {
        LocalDateTime now = LocalDateTime.now();

        Member member = memberMapper.fromMemberDTOtoMember(memberDTO);
        memberRepository.save(member);
    }

    public List<MemberDTO> findAllMemberByMemberType(MemberType memberType) {

        List<Member> allMember = memberRepository.findByMemberFlagTrueAndMemberType(memberType);

        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (Member member : allMember) {
            memberDTOList.add(memberMapper.fromMembertoMemberDTO(member));
        }

        return memberDTOList;
    }
}
