package intbyte4.learnsmate.blacklist.service;

import intbyte4.learnsmate.blacklist.domain.dto.BlacklistDTO;
import intbyte4.learnsmate.blacklist.domain.dto.BlacklistReportCommentDTO;
import intbyte4.learnsmate.blacklist.domain.entity.Blacklist;
import intbyte4.learnsmate.blacklist.mapper.BlacklistMapper;
import intbyte4.learnsmate.blacklist.repository.BlacklistRepository;
import intbyte4.learnsmate.comment.domain.dto.CommentDTO;
import intbyte4.learnsmate.comment.service.CommentService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.report.domain.dto.ReportDTO;
import intbyte4.learnsmate.report.domain.dto.ReportedMemberDTO;
import intbyte4.learnsmate.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlacklistService {

    private final BlacklistRepository blacklistRepository;
    private final BlacklistMapper blacklistMapper;
    private final ReportService reportService;
    private final MemberService memberService;
    private final CommentService commentService;

    @Autowired
    public BlacklistService(BlacklistRepository blacklistRepository, BlacklistMapper blacklistMapper, ReportService reportService, MemberService memberService, CommentService commentService) {
        this.blacklistRepository = blacklistRepository;
        this.blacklistMapper = blacklistMapper;
        this.reportService = reportService;
        this.memberService = memberService;
        this.commentService = commentService;
    }

    // 1. flag는 볼필요 없음. -> 학생, 강사만 구분해야함.
    public List<BlacklistDTO> findAllBlacklistByMemberType(MemberType memberType) {

        List<Blacklist> blacklistList = blacklistRepository.findAllBlacklistByMemberType(memberType);

        List<BlacklistDTO> blacklistDTOList = new ArrayList<>();

//                .memberCode(blacklist.getMemberCode())
//                .reportCode(blacklist.getReportCode())
//                .adminCode(blacklist.getAdminCode())

        for (Blacklist blacklist : blacklistList) {
            // Blacklsit -> BlacklsitDTO
            blacklistDTOList.add(blacklistMapper.fromBlacklistToBlacklistDTO(blacklist));
        }

        return blacklistDTOList;
    }

    // 1. 멤버 타입에 따라 신고내역 횟수 뒤져서 찾기 reportService.findCount
    // (피신고자 코드의 횟수만 가져오면 됨. -> 피신고자 멤버코드, 신고 횟수)
    // 2. Member table에서 가져오기(true인 놈들)
    public List<ReportedMemberDTO> findAllReservedBlacklistByMemberType(MemberType memberType) {

        List<ReportedMemberDTO> reportedMoreThanFiveMemberList = reportService.findReportCountByMemberCode();

        return reportedMoreThanFiveMemberList;
    }

    // 신고당한 댓글 내역까지 모두 볼수 있는 서비스 메서드
    public List<BlacklistReportCommentDTO> findMemberReservedBlacklist(Long memberCode) {

        // 1. Report table에서 memberCode와 reportedMemberCode 가 같은거 가져오기
        List<ReportDTO> reportDTOlist = reportService.findAllReportByMemberCode(memberCode);

        // 2. ReportDTO의 comment_code 내역 가져오기 -> comment table
        List<CommentDTO> commentDTOList = reportDTOlist.stream()
                .map(reportDTO -> commentService.findComentByCommentCode(reportDTO.getCommentCode()))
                .collect(Collectors.toList());

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
}
