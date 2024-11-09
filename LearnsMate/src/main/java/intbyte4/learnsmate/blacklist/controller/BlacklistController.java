package intbyte4.learnsmate.blacklist.controller;

import intbyte4.learnsmate.blacklist.domain.dto.BlacklistDTO;
import intbyte4.learnsmate.blacklist.domain.dto.BlacklistReportCommentDTO;
import intbyte4.learnsmate.blacklist.domain.vo.response.ResponseFindReportVO;
import intbyte4.learnsmate.blacklist.domain.vo.response.ResponseFindReservedBlacklistOneVO;
import intbyte4.learnsmate.blacklist.domain.vo.response.ResponseFindReservedStudentBlacklistVO;
import intbyte4.learnsmate.blacklist.domain.vo.response.ResponseFindReservedTutorBlacklistVO;
import intbyte4.learnsmate.blacklist.mapper.BlacklistMapper;
import intbyte4.learnsmate.blacklist.service.BlacklistService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.report.domain.dto.ReportedMemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/blacklist")
public class BlacklistController {

    private final BlacklistService blacklistService;
    private final BlacklistMapper blacklistMapper;

    @Autowired
    public BlacklistController(BlacklistService blacklistService, BlacklistMapper blacklistMapper) {
        this.blacklistService = blacklistService;
        this.blacklistMapper = blacklistMapper;
    }

    // 1. 모든 학생 블랙리스트 조회
    @GetMapping("/student")
    public ResponseEntity<List<ResponseFindReportVO>> findAllStudentBlacklist() {

        // service에서 dto 반환
        List<BlacklistDTO> blacklistDTOList = blacklistService.findAllBlacklistByMemberType(MemberType.STUDENT);

        List<ResponseFindReportVO> response = new ArrayList<>();

        // dto -> ResponseFindReportVO로 전환해주기
        for(BlacklistDTO blacklistDTO : blacklistDTOList) {
            response.add(blacklistMapper.fromBlacklistDTOToResponseFindReportVO(blacklistDTO));
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 2. 모든 강사 블랙리스트 조회
    @GetMapping("/tutor")
    public ResponseEntity<List<ResponseFindReportVO>> findAllTutorBlacklist() {

        // service에서 dto 반환
        List<BlacklistDTO> blacklistDTOList = blacklistService.findAllBlacklistByMemberType(MemberType.TUTOR);

        List<ResponseFindReportVO> response = new ArrayList<>();

        // dto -> ResponseFindReportVO로 전환해주기
        for(BlacklistDTO blacklistDTO : blacklistDTOList) {
            response.add(blacklistMapper.fromBlacklistDTOToResponseFindReportVO(blacklistDTO));
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 직원 - 예비 블랙리스트 전체 조회(학생)
    @GetMapping("/student/reserved")
    public ResponseEntity<List<ResponseFindReservedStudentBlacklistVO>> findAllStudentReservedBlacklist() {
        // dto로 받아와야하는데 어떤 dto로 받아올까?
        // 학생코드, 학생명, 누적 신고 횟수 이렇게가 필요함. -> dto 하나 만들자.
        List<ReportedMemberDTO> reservedBlacklistDTOList
                = blacklistService.findAllReservedBlacklistByMemberType(MemberType.STUDENT);

        // ReportedMemberDTO -> ResponseFindReservedStudentBlacklistVO
        List<ResponseFindReservedStudentBlacklistVO> response = new ArrayList<>();
        for(ReportedMemberDTO reservedBlacklistDTO : reservedBlacklistDTOList) {
            response.add(
                    blacklistMapper.fromReportedMemberDTOToResponseFindReservedStudentBlacklistVO(reservedBlacklistDTO)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 직원 - 예비 블랙리스트 전체 조회(강사)
    @GetMapping("/tutor/reserved")
    public ResponseEntity<List<ResponseFindReservedTutorBlacklistVO>> findAllTutorReservedBlacklist(){
        // dto로 받아와야하는데 어떤 dto로 받아올까?
        // 강사코드, 강사명, 누적 신고 횟수 이렇게가 필요함. -> dto 하나 만들자.
        List<ReportedMemberDTO> reservedBlacklistDTOList
                = blacklistService.findAllReservedBlacklistByMemberType(MemberType.TUTOR);

        // ReportedMemberDTO -> ResponseFindReservedTutorBlacklistVO
        List<ResponseFindReservedTutorBlacklistVO> response = new ArrayList<>();

        for(ReportedMemberDTO reservedBlacklistDTO : reservedBlacklistDTOList) {
            response.add(
                    blacklistMapper.fromReportedMemberDTOToResponseFindReservedTutorBlacklistVO(reservedBlacklistDTO)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 학생 예비 블랙리스트 단건 조회
    @GetMapping("/student/reserved/{studentcode}")
    public ResponseEntity<List<ResponseFindReservedBlacklistOneVO>> findStudentReservedBlacklist(@PathVariable("studentcode") Long studentCode) {
        // 결국 예비 블랙리스트가 없어서 계산해서 가져와야함. -> Report에서 tutorcode에 해당하는 모든 Report 가져오고
        // -> 그 report 안에 있는 comment code를 통해서 comment도 가져와야함.
        List<BlacklistReportCommentDTO> dtoList = blacklistService.findMemberReservedBlacklist(studentCode);

        List<ResponseFindReservedBlacklistOneVO> voList
                = blacklistMapper.fromBlacklistReportCommentDTOToResponseFindReservedBlacklistOneVO(dtoList);

        return ResponseEntity.status(HttpStatus.OK).body(voList);
    }

    // 강사 예비 블랙리스트 단건 조회
    @GetMapping("/tutor/reserved/{tutorcode}")
    public ResponseEntity<List<ResponseFindReservedBlacklistOneVO>> findTutorReservedBlacklist(@PathVariable("tutorcode") Long tutorCode) {
        // 결국 예비 블랙리스트가 없어서 계산해서 가져와야함. -> Report에서 tutorcode에 해당하는 모든 Report 가져오고
        // -> 그 report 안에 있는 comment code를 통해서 comment도 가져와야함.
        List<BlacklistReportCommentDTO> dtoList = blacklistService.findMemberReservedBlacklist(tutorCode);

        List<ResponseFindReservedBlacklistOneVO> voList
                = blacklistMapper.fromBlacklistReportCommentDTOToResponseFindReservedBlacklistOneVO(dtoList);

        return ResponseEntity.status(HttpStatus.OK).body(voList);
    }
}
