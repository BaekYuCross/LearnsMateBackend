package intbyte4.learnsmate.blacklist.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.domain.entity.CustomUserDetails;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.blacklist.domain.dto.*;
import intbyte4.learnsmate.blacklist.domain.entity.Blacklist;
import intbyte4.learnsmate.blacklist.domain.vo.response.ResponseFindBlacklistVO;
import intbyte4.learnsmate.blacklist.domain.vo.response.ResponseFindReservedStudentBlacklistVO;
import intbyte4.learnsmate.blacklist.mapper.BlacklistMapper;
import intbyte4.learnsmate.blacklist.repository.BlacklistRepository;
import intbyte4.learnsmate.comment.domain.dto.CommentDTO;
import intbyte4.learnsmate.comment.service.CommentService;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.report.domain.dto.ReportDTO;
import intbyte4.learnsmate.report.domain.dto.ReportedMemberDTO;
import intbyte4.learnsmate.report.repository.ReportRepository;
import intbyte4.learnsmate.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlacklistService {

    private final BlacklistRepository blacklistRepository;
    private final BlacklistMapper blacklistMapper;
    private final ReportService reportService;
    private final MemberService memberService;
    private final CommentService commentService;
    private final MemberMapper memberMapper;
    private final AdminMapper adminMapper;
    private final AdminService adminService;
    private final ReportRepository reportRepository;

    // 1. flag는 볼필요 없음. -> 학생, 강사만 구분해야함.
    public BlacklistPageResponse<ResponseFindBlacklistVO> findAllBlacklistByMemberType(int page, int size, MemberType memberType) {

        // Pageable 객체 생성
        PageRequest pageable = PageRequest.of(page, size);

        // 페이징 처리된 데이터 조회
        Page<BlacklistDTO> blacklistPage = blacklistRepository.findAllBlacklistByMemberType(memberType, pageable);

        // DTO -> VO 변환
        List<ResponseFindBlacklistVO> responseList = blacklistPage.getContent().stream()
                .map(blacklistMapper::fromBlacklistDTOtoResponseFindBlacklistVO)
                .collect(Collectors.toList());

        // 페이지 응답 생성
        return new BlacklistPageResponse<>(
                responseList,
                blacklistPage.getTotalElements(),
                blacklistPage.getTotalPages(),
                blacklistPage.getNumber(),
                blacklistPage.getSize()
        );
    }

    // 1. flag는 볼필요 없음. -> 학생, 강사만 구분해야함.
    public BlacklistPageResponse<ResponseFindBlacklistVO> findAllBlacklistByMemberTypeBySort(
            int page, int size, MemberType memberType, String sortField, String sortDirection) {

        Sort sort = Sort.by(
                sortDirection.equalsIgnoreCase("DESC") ?
                        Sort.Direction.DESC : Sort.Direction.ASC,
                sortField
        );

        // Pageable 객체 생성
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<BlacklistDTO> blacklistPage = blacklistRepository.findAllBlacklistByMemberTypeBySort(memberType, pageable);

        // DTO -> VO 변환
        List<ResponseFindBlacklistVO> responseList = blacklistPage.getContent().stream()
                .map(blacklistMapper::fromBlacklistDTOtoResponseFindBlacklistVO)
                .collect(Collectors.toList());

        // 페이지 응답 생성
        return new BlacklistPageResponse<>(
                responseList,
                blacklistPage.getTotalElements(),
                blacklistPage.getTotalPages(),
                blacklistPage.getNumber(),
                blacklistPage.getSize()
        );
    }

    // 1. 멤버 타입에 따라 신고내역 횟수 뒤져서 찾기 reportService.findCount
    // (피신고자 코드의 횟수만 가져오면 됨. -> 피신고자 멤버코드, 신고 횟수)
    // 2. Member table에서 가져오기(true인 놈들)
    public ReservedBlacklistPageResponse<?> findAllReservedBlacklistByMemberType(
            int page, int size, MemberType memberType)
    {

        Pageable pageable = PageRequest.of(page, size);

        // 모든 멤버 가져옴 (페이지네이션 포함)
        Page<ReportedMemberDTO> reportedMemberPage = reportService.findReportCountByMemberCode(memberType, pageable);

        // 멤버 타입이 동일하고 flag가 true인 사람만 필터링
        List<ReportedMemberDTO> filteredList = reportedMemberPage.stream()
                .filter(dto -> dto.getReportedMember().getMemberType().equals(memberType)
                        && dto.getReportedMember().getMemberFlag())
                .toList();

        // Page로 다시 변환
        Page<ReportedMemberDTO> filteredPage = new PageImpl<>(filteredList, pageable, filteredList.size());

        // DTO -> VO 변환
        List<ResponseFindReservedStudentBlacklistVO> content = filteredPage.getContent().stream()
                .map(blacklistMapper::fromReportedMemberDTOToResponseFindReservedStudentBlacklistVO)
                .toList();

        return new ReservedBlacklistPageResponse<>(
                content,
                filteredPage.getTotalElements(),
                filteredPage.getTotalPages(),
                page,
                size
        );
    }

    // 컬럼 정렬 적용
    public ReservedBlacklistPageResponse<?> findAllReservedBlacklistByMemberTypeWithSort(
            int page, int size, MemberType memberType, String sortField, String sortDirection
    ) {
        Sort sort = createSort(sortField, sortDirection);

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ReportedMemberDTO> reportedMemberPage = reportService.findReportCountByMemberCode(memberType, pageable);

        List<ResponseFindReservedStudentBlacklistVO> content = reportedMemberPage.getContent().stream()
                .map(blacklistMapper::fromReportedMemberDTOToResponseFindReservedStudentBlacklistVO)
                .toList();

        return new ReservedBlacklistPageResponse<>(
                content,
                reportedMemberPage.getTotalElements(),
                reportedMemberPage.getTotalPages(),
                page,
                size
        );
    }
    private Sort createSort(String sortField, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        return switch (sortField) {
            case "memberName" -> Sort.by(direction, "reportedMember.memberName");
            case "memberCode" -> Sort.by(direction, "reportedMember.memberCode");
            case "reportCount" -> JpaSort.unsafe(direction, "COUNT(r)");
            default -> JpaSort.unsafe(Sort.Direction.DESC, "COUNT(r)");  // 기본 정렬
        };
    }

    // 블랙리스트에서 신고당한 댓글 내역까지 모두 볼수 있는 서비스 메서드
    public List<BlacklistReportCommentDTO> findBlacklistReportComment(Long blacklistCode, Long mCode) {
        Long memberCode = null;
        if(mCode == null && blacklistCode != null){
            memberCode = blacklistRepository.findById(blacklistCode).get().getMember().getMemberCode();
        }else if(blacklistCode == null && mCode != null){
            memberCode = mCode;
        }

        if(memberCode == null)
            throw new CommonException(StatusEnum.USER_NOT_FOUND);

        // 1. Report table에서 memberCode와 reportedMemberCode 가 같은거 가져오기
        List<ReportDTO> reportDTOlist = reportService.findAllReportByMemberCode(memberCode);

        // 2. ReportDTO의 comment_code 내역 가져오기 -> comment table
        List<CommentDTO> commentDTOList = reportDTOlist.stream()
                .map(reportDTO -> commentService.findCommentByCommentCode(reportDTO.getCommentCode()))
                .toList();

        // 3. List<BlacklistReportCommentDTO> 생성 및 데이터 추가
        List<BlacklistReportCommentDTO> blacklistReportCommentDTOList = new ArrayList<>();
        for (int i = 0; i < reportDTOlist.size(); i++) {
            blacklistReportCommentDTOList.add(BlacklistReportCommentDTO.builder()
                    .reportDTO(reportDTOlist.get(i))
                    .commentDTO(commentDTOList.get(i))
                    .build());
        }
        return blacklistReportCommentDTOList;
    }

    // 블랙리스트 등록 메서드
    public void addMemberToBlacklist(BlacklistDTO dto) {
        // 1. memberCode에 해당하는 사람
        MemberDTO memberDTO = memberService.findById(dto.getMemberCode());
        Member member = memberMapper.fromMemberDTOtoMember(memberDTO);

        // 2. admin
        AdminDTO adminDTO = adminService.findByAdminCode(getAdminCode());
        Admin admin = adminMapper.toEntity(adminDTO);

        // 3. BlacklistDTO 생성 -> 이유만 있으면 됨. -> 이유도 넘겨받아야함. -> dto 자체를 넘겨받으면 해결
        // 블랙리스트로 저장해야하니까 Blacklist 엔티티를 만들어야함.
        Blacklist blacklist = blacklistMapper.fromBlacklistDTOtoBlacklist(dto, member, admin);

        // 4. 블랙리스트에 저장
        blacklistRepository.save(blacklist);

        // 5. 회원 flag false로 수정
        memberService.deleteMember(dto.getMemberCode());
    }

    // 블랙리스트 필터링 메서드
    public BlacklistPageResponse<ResponseFindBlacklistVO> filterBlacklistMember(BlacklistFilterRequestDTO dto, int page, int size, String sortField, String sortDirection){
        Sort sort = Sort.by(
                sortDirection.equalsIgnoreCase("DESC") ?
                        Sort.Direction.DESC : Sort.Direction.ASC,
                sortField
        );
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Blacklist> blacklistPage = blacklistRepository.searchBy(dto, pageable);

        List<ResponseFindBlacklistVO> blacklistVOList = blacklistPage.getContent().stream()
                .map(blacklistMapper::fromBlacklistToResponseFindBlacklistVO)
                .collect(Collectors.toList());

        return new BlacklistPageResponse<>(
                blacklistVOList,
                blacklistPage.getTotalElements(),
                blacklistPage.getTotalPages(),
                blacklistPage.getNumber() + 1,
                blacklistPage.getSize()
        );
    }

    // filterDTO에서 MEMBER TYPE SET 함
    public List<BlacklistDTO> findAllByFilterWithExcel(BlacklistFilterRequestDTO filterDTO){
        List<Blacklist> blacklistList = blacklistRepository.searchByWithoutPaging(filterDTO);

        return blacklistList.stream()
                .map(blacklistMapper::fromBlacklistToBlacklistDTO)
                .collect(Collectors.toList());
    }

    public List<BlacklistDTO> findAllByMemberTypeWithExcel(MemberType memberType){
        List<Blacklist> blacklistList = blacklistRepository.findAllByMemberTypeWithExcel(memberType);

        return blacklistList.stream()
                .map(blacklistMapper::fromBlacklistToBlacklistDTO)
                .collect(Collectors.toList());
    }

    public Long getAdminCode() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new CommonException(StatusEnum.USER_NOT_FOUND);
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof String) {
            throw new CommonException(StatusEnum.USER_NOT_FOUND);
        }

        if (principal instanceof CustomUserDetails userDetails) {
            log.info("Authentication: {}", authentication);
            log.info("userDetails: {}", userDetails.toString());
            return userDetails.getUserDTO().getAdminCode();
        }

        throw new CommonException(StatusEnum.USER_NOT_FOUND);
    }
}
