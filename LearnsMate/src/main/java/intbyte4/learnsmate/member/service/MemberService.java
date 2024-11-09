package intbyte4.learnsmate.member.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.member.mapper.MemberMapper;
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

    // 나현이가 필요한 강사 코드로 강사명 찾아오기
    public String findTutorByMemberCode(Long memberCode){

        String tutorName = memberRepository.findMemberNameByMemberCode(memberCode)
                .orElseThrow(RuntimeException::new);

        return tutorName;
    }

    public List<MemberDTO> findMemberListByStudentCode(Long memberCode) {
        List<Member> member = memberRepository.finByMemberFlagTrueAndMemberCode(memberCode);
        return member.stream()
                .map(memberMapper::fromMembertoMemberDTO)
                .toList();
    }

    public Member findByStudentCode(Long memberCode) {
        Member student = memberRepository.findById(memberCode).orElseThrow(() -> new CommonException(StatusEnum.STUDENT_NOT_FOUND));
        if (!student.getMemberType().equals(MemberType.STUDENT)) throw new CommonException(StatusEnum.RESTRICTED);
        return student;
    }
}
