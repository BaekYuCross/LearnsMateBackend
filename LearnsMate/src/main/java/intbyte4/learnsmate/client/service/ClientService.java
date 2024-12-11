package intbyte4.learnsmate.client.service;

import intbyte4.learnsmate.client.domain.dto.ClientLoginDTO;
import intbyte4.learnsmate.client.domain.dto.ClientLoginHistoryDTO;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.login_history.domain.dto.LoginHistoryDTO;
import intbyte4.learnsmate.login_history.service.LoginHistoryService;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;
    private final LoginHistoryService loginHistoryService;

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
    public ClientLoginHistoryDTO loginMember(ClientLoginDTO loginDTO) {
        log.debug("Received login request for email: {}", loginDTO.getMemberEmail());

        Member member = memberRepository.findByMemberEmail(loginDTO.getMemberEmail());
        log.debug("Member found: {}", member != null);

        if(member == null) {
            log.debug("User not found with email: {}", loginDTO.getMemberEmail());
            throw new CommonException(StatusEnum.USER_NOT_FOUND);
        }

        boolean passwordMatches = passwordEncoder.matches(loginDTO.getMemberPassword(), member.getMemberPassword());
        log.debug("Password matches: {}", passwordMatches);

        if(!passwordMatches) {
            log.debug("Invalid password for user: {}", loginDTO.getMemberEmail());
            throw new CommonException(StatusEnum.INVALID_PASSWORD);
        }

        if(!member.getMemberFlag()){
            throw new CommonException(StatusEnum.BLACKLIST_MEMBER);
        }

        LoginHistoryDTO loginHistoryDTO = LoginHistoryDTO.builder()
                .lastLoginDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .memberCode(member.getMemberCode())
                .build();

        // 여기서 loginhistory 호출
        Long loginHistoryCode = loginHistoryService.saveLoginHistory(loginHistoryDTO);

        ClientLoginHistoryDTO clientLoginHistoryDTO = new ClientLoginHistoryDTO(
            member.getMemberCode(), member.getMemberName(), member.getMemberEmail(), loginHistoryCode
        );

        return clientLoginHistoryDTO;
    }

    // 회원 로그아웃시 로그아웃시간 저장
    public void logoutMember(Long loginHistoryCode) {
        loginHistoryService.saveLogoutTime(loginHistoryCode);
    }

    // BCrypt로 암호화된 비밀번호인지 확인
    private boolean isPasswordEncrypted(String password) {
        return password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$");
    }
}
