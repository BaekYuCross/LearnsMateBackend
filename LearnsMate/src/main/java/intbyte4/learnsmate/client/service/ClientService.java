package intbyte4.learnsmate.client.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;

    // 회원 저장
    public void saveMember(MemberDTO memberDTO) {

        // 비밀번호가 있고, 암호화되어 있지 않은 경우에만 암호화
        if (memberDTO.getMemberPassword() != null && !isPasswordEncrypted(memberDTO.getMemberPassword())) {
            memberDTO.setMemberPassword(passwordEncoder.encode(memberDTO.getMemberPassword()));
        }
        Member member = memberMapper.fromMemberDTOtoMember(memberDTO);
        memberRepository.save(member);
    }

    // 회원 로그인
    public MemberDTO loginMember(MemberDTO memberDTO) {
        if (memberDTO.getMemberPassword() != null && !isPasswordEncrypted(memberDTO.getMemberPassword())) {
            memberDTO.setMemberPassword(passwordEncoder.encode(memberDTO.getMemberPassword()));
        }

        Member member = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());

        if(member == null) {
            throw new CommonException(StatusEnum.USER_NOT_FOUND);
        } else if(!member.getMemberPassword().equals(memberDTO.getMemberPassword())) {
            throw new CommonException(StatusEnum.INVALID_PASSWORD);
        }

        MemberDTO findMemberDTO = memberMapper.fromMembertoMemberDTO(member);

        return findMemberDTO;
    }

    // BCrypt로 암호화된 비밀번호인지 확인
    private boolean isPasswordEncrypted(String password) {
        return password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$");
    }
}
