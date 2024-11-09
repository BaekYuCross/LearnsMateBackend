package intbyte4.learnsmate.blacklist.service;

import intbyte4.learnsmate.blacklist.domain.dto.BlacklistDTO;
import intbyte4.learnsmate.blacklist.domain.entity.Blacklist;
import intbyte4.learnsmate.blacklist.mapper.BlacklistMapper;
import intbyte4.learnsmate.blacklist.repository.BlacklistRepository;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.report.domain.dto.ReportedMemberDTO;
import intbyte4.learnsmate.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlacklistService {

    private final BlacklistRepository blacklistRepository;
    private final BlacklistMapper blacklistMapper;
    private final ReportService reportService;
    private final MemberService memberService;

    @Autowired
    public BlacklistService(BlacklistRepository blacklistRepository, BlacklistMapper blacklistMapper, ReportService reportService, MemberService memberService) {
        this.blacklistRepository = blacklistRepository;
        this.blacklistMapper = blacklistMapper;
        this.reportService = reportService;
        this.memberService = memberService;
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
}
