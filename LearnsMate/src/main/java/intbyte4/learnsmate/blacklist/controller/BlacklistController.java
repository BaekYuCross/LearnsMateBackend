package intbyte4.learnsmate.blacklist.controller;

import intbyte4.learnsmate.blacklist.domain.dto.BlacklistDTO;
import intbyte4.learnsmate.blacklist.domain.vo.response.ResponseFindReportVO;
import intbyte4.learnsmate.blacklist.mapper.BlacklistMapper;
import intbyte4.learnsmate.blacklist.service.BlacklistService;
import intbyte4.learnsmate.member.domain.MemberType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
