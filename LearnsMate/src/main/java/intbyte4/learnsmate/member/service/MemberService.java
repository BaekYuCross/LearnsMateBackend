package intbyte4.learnsmate.member.service;

import intbyte4.learnsmate.campaign.domain.dto.FindCampaignDetailDTO;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.member.domain.dto.MemberFilterRequestDTO;
import intbyte4.learnsmate.member.domain.pagination.MemberPageResponse;
import intbyte4.learnsmate.member.domain.vo.response.ResponseFindMemberVO;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

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
    public void loginMember(MemberDTO memberDTO) {
        if (memberDTO.getMemberPassword() != null && !isPasswordEncrypted(memberDTO.getMemberPassword())) {
            memberDTO.setMemberPassword(passwordEncoder.encode(memberDTO.getMemberPassword()));
        }

        Member member = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());

        if(member == null) {
            throw new CommonException(StatusEnum.USER_NOT_FOUND);
        } else if(!passwordEncoder.matches(memberDTO.getMemberPassword(), member.getMemberPassword())) {
            throw new CommonException(StatusEnum.INVALID_PASSWORD);
        }
    }

    public MemberPageResponse<ResponseFindMemberVO> findAllMemberByMemberType(int page, int size, MemberType memberType) {

        // Pageable 객체 생성
        PageRequest pageable = PageRequest.of(page, size);
        // 페이징 처리된 데이터 조회
        Page<Member> memberPage = memberRepository.findByMemberType(memberType, pageable);

        // Member -> ResponseFindMemberVO 변환
        List<ResponseFindMemberVO> responseVOList = memberPage.getContent().stream()
                .map(memberMapper::fromMemberToResponseFindMemberVO)
                .collect(Collectors.toList());

        // MemberPageResponse 반환
        return new MemberPageResponse<>(
                responseVOList,
                memberPage.getTotalElements(),
                memberPage.getTotalPages(),
                memberPage.getNumber(),
                memberPage.getSize()
        );
    }

    public MemberDTO findByStudentCode(Long memberCode) {
        Member student = memberRepository.findById(memberCode).orElseThrow(() -> new CommonException(StatusEnum.STUDENT_NOT_FOUND));
        if (!student.getMemberType().equals(MemberType.STUDENT)) throw new CommonException(StatusEnum.RESTRICTED);
        return memberMapper.fromMembertoMemberDTO(student);
    }

    // 멤버 회원정보 수정 메서드
    public void editMember(MemberDTO memberDTO) {
        Member member = memberMapper.fromMemberDTOtoMember(memberDTO);
        memberRepository.findById(member.getMemberCode())
                .orElseThrow(() -> new CommonException(StatusEnum.USER_NOT_FOUND));

        memberRepository.save(member);
    }

    // 멤버 회원 삭제 flag false 메서드
    public void deleteMember(Long memberCode) {
        Member member = memberRepository.findById(memberCode)
                .orElseThrow(() -> new CommonException(StatusEnum.USER_NOT_FOUND));

        member.deactivate();

        memberRepository.save(member);
    }

    // memberCode, memberType -> memberDTO로 반환 메서드
    public MemberDTO findMemberByMemberCode(Long memberCode, MemberType memberType) {
        Member member = memberRepository.findByMemberCodeAndMemberType(memberCode, memberType);

        if(memberType.equals(MemberType.STUDENT) && member.getMemberType().equals(MemberType.TUTOR)){ // 학생을 찾는데 강사인 경우
             throw new CommonException(StatusEnum.ENUM_NOT_MATCH);
        }else if(memberType.equals(MemberType.TUTOR) && member.getMemberType().equals(MemberType.STUDENT)){ // 강사를 찾는 경우
             throw new CommonException(StatusEnum.ENUM_NOT_MATCH);
        }

        return memberMapper.fromMembertoMemberDTO(member);
    }

    // 멤버 타입과 상관 없이 멤버 코드로 조회하는 메서드
    public MemberDTO findById(Long memberCode){
        Member member = memberRepository.findById(memberCode)
                .orElseThrow(() -> new CommonException(StatusEnum.USER_NOT_FOUND));

        return memberMapper.fromMembertoMemberDTO(member);
    }

    // 캠페인 코드로 멤버를 조회하는 메서드 (Campaign에서 사용)
    public Page<MemberDTO> findMembersByCampaignCode(FindCampaignDetailDTO campaignDTO, Pageable pageable) {
        return memberRepository.findMembersByCampaignCode(campaignDTO.getCampaignCode(), pageable)
                .map(memberMapper::fromMembertoMemberDTO);
    }

    // 학생 필터링하는 서비스 코드
    public MemberPageResponse<ResponseFindMemberVO> filterStudent(MemberFilterRequestDTO dto, int page, int size){
        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size);

        // 필터 조건과 페이징 처리된 데이터 조회
        Page<Member> memberPage = memberRepository.searchBy(dto, pageable);

        // DTO 리스트로 변환
        List<ResponseFindMemberVO> memberVOList = memberPage.getContent().stream()
                .map(memberMapper::fromMemberToResponseFindMemberVO)
                .collect(Collectors.toList());

        // MemberPageResponse 생성 후 반환
        return new MemberPageResponse<>(
                memberVOList,               // 데이터 리스트
                memberPage.getTotalElements(), // 전체 데이터 수
                memberPage.getTotalPages(),    // 전체 페이지 수
                memberPage.getNumber() + 1,    // 현재 페이지 (0-based → 1-based)
                memberPage.getSize()           // 페이지 크기
        );
    }

    // 강사 필터링하는 코드 -> 강사 필터링은 조건이 더 적음(멤버에 다 포함됨)
    public MemberPageResponse<ResponseFindMemberVO> filterTutor(MemberFilterRequestDTO dto, int page, int size){
        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size);

        // 필터 조건과 페이징 처리된 데이터 조회
        Page<Member> memberPage = memberRepository.searchBy(dto, pageable);

        // DTO 리스트로 변환
        List<ResponseFindMemberVO> memberVOList = memberPage.getContent().stream()
                .map(memberMapper::fromMemberToResponseFindMemberVO)
                .collect(Collectors.toList());

        // MemberPageResponse 생성 후 반환
        return new MemberPageResponse<>(
                memberVOList,               // 데이터 리스트
                memberPage.getTotalElements(), // 전체 데이터 수
                memberPage.getTotalPages(),    // 전체 페이지 수
                memberPage.getNumber() + 1,    // 현재 페이지 (0-based → 1-based)
                memberPage.getSize()           // 페이지 크기
        );
    }

    public List<MemberDTO> findAllByFilterWithExcel(MemberFilterRequestDTO filterDTO){
        List<Member> memberList = memberRepository.searchByWithoutPaging(filterDTO);

        return memberList.stream()
                .map(memberMapper::fromMembertoMemberDTO)
                .collect(Collectors.toList());
    }

    public List<MemberDTO> findAllByMemberTypeWithExcel(MemberType memberType){
        List<Member> memberList = memberRepository.findAllByMemberTypeWithExcel(memberType);

        return memberList.stream()
                .map(memberMapper::fromMembertoMemberDTO)
                .collect(Collectors.toList());
    }

    // BCrypt로 암호화된 비밀번호인지 확인
    private boolean isPasswordEncrypted(String password) {
        return password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$");
    }

    public int findYesterdayCountByMemberType(MemberType memberType) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return memberRepository.countByMemberTypeAndCreatedAtBetween(
                memberType, yesterday.atStartOfDay(), yesterday.plusDays(1).atStartOfDay()
        );
    }

    public int findTotalCountByMemberType(MemberType memberType) {
        return memberRepository.countByMemberType(memberType);
    }
}
