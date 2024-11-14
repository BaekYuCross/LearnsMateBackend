package intbyte4.learnsmate.blacklist.controller;

import intbyte4.learnsmate.blacklist.domain.dto.BlacklistDTO;
import intbyte4.learnsmate.blacklist.domain.dto.BlacklistReportCommentDTO;
import intbyte4.learnsmate.blacklist.domain.vo.request.RequestSaveBlacklistVO;
import intbyte4.learnsmate.blacklist.domain.vo.response.ResponseFindBlacklistVO;
import intbyte4.learnsmate.blacklist.domain.vo.response.ResponseFindReservedBlacklistOneVO;
import intbyte4.learnsmate.blacklist.domain.vo.response.ResponseFindReservedStudentBlacklistVO;
import intbyte4.learnsmate.blacklist.domain.vo.response.ResponseFindReservedTutorBlacklistVO;
import intbyte4.learnsmate.blacklist.mapper.BlacklistMapper;
import intbyte4.learnsmate.blacklist.service.BlacklistService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.report.domain.dto.ReportedMemberDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blacklist")
public class BlacklistController {

    private final BlacklistService blacklistService;
    private final BlacklistMapper blacklistMapper;

    // 1. 모든 학생 블랙리스트 조회
    @Operation(summary = "직원 - 학생 블랙리스트 전체 조회")
    @GetMapping("/student")
    public ResponseEntity<List<ResponseFindBlacklistVO>> findAllStudentBlacklist() {

        // service에서 dto 반환
        List<BlacklistDTO> blacklistDTOList = blacklistService.findAllBlacklistByMemberType(MemberType.STUDENT);

        List<ResponseFindBlacklistVO> response = new ArrayList<>();

        // dto -> ResponseFindBlacklistVO로 전환해주기
        for(BlacklistDTO blacklistDTO : blacklistDTOList) {
            response.add(blacklistMapper.fromBlacklistDTOToResponseFindReportVO(blacklistDTO));
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 2. 모든 강사 블랙리스트 조회
    @Operation(summary = "직원 - 강사 블랙리스트 전체 조회")
    @GetMapping("/tutor")
    public ResponseEntity<List<ResponseFindBlacklistVO>> findAllTutorBlacklist() {

        // service에서 dto 반환
        List<BlacklistDTO> blacklistDTOList = blacklistService.findAllBlacklistByMemberType(MemberType.TUTOR);

        List<ResponseFindBlacklistVO> response = new ArrayList<>();

        // dto -> ResponseFindBlacklistVO로 전환해주기
        for(BlacklistDTO blacklistDTO : blacklistDTOList) {
            response.add(blacklistMapper.fromBlacklistDTOToResponseFindReportVO(blacklistDTO));
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 3. 학생 블랙리스트 단건 조회
    @Operation(summary = "직원 - 학생 블랙리스트 단건 세부 조회")
    @GetMapping("/student/{studentcode}")
    public ResponseEntity<List<ResponseFindReservedBlacklistOneVO>> findStudentBlacklist(
            @PathVariable("studentcode") Long studentCode
    ) {
        // 보여줘야 하는것들
        // 학생 정보 + 신고정보 + 댓글정보 -> 예비 블랙리스트하고 똑같이 하면 될거같네
        List<BlacklistReportCommentDTO> dtoList = blacklistService.findBlacklistReportComment(studentCode);

        List<ResponseFindReservedBlacklistOneVO> voList
                = blacklistMapper.fromBlacklistReportCommentDTOToResponseFindReservedBlacklistOneVO(dtoList);

        return ResponseEntity.status(HttpStatus.OK).body(voList);
    }

    // 4. 강사 블랙리스트 단건 조회
    @Operation(summary = "직원 - 강사 블랙리스트 단건 세부 조회")
    @GetMapping("/tutor/{tutorcode}")
    public ResponseEntity<List<ResponseFindReservedBlacklistOneVO>> findTutorBlacklist(
            @PathVariable("tutorcode") Long tutorCode
    ) {
        List<BlacklistReportCommentDTO> dtoList = blacklistService.findBlacklistReportComment(tutorCode);

        List<ResponseFindReservedBlacklistOneVO> voList
                = blacklistMapper.fromBlacklistReportCommentDTOToResponseFindReservedBlacklistOneVO(dtoList);

        return ResponseEntity.status(HttpStatus.OK).body(voList);
    }

    // 직원 - 예비 블랙리스트 전체 조회(학생)
    @Operation(summary = "직원 - 학생 예비 블랙리스트 전체 조회")
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
    @Operation(summary = "직원 - 강사 예비 블랙리스트 전체 조회")
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
    @Operation(summary = "직원 - 학생 예비 블랙리스트 단건 세부 조회")
    @GetMapping("/student/reserved/{studentcode}")
    public ResponseEntity<List<ResponseFindReservedBlacklistOneVO>> findStudentReservedBlacklist(
            @PathVariable("studentcode") Long studentCode
    ) {
        // 결국 예비 블랙리스트가 없어서 계산해서 가져와야함. -> Report에서 tutorcode에 해당하는 모든 Report 가져오고
        // -> 그 report 안에 있는 comment code를 통해서 comment도 가져와야함.
        List<BlacklistReportCommentDTO> dtoList = blacklistService.findBlacklistReportComment(studentCode);

        List<ResponseFindReservedBlacklistOneVO> voList
                = blacklistMapper.fromBlacklistReportCommentDTOToResponseFindReservedBlacklistOneVO(dtoList);

        return ResponseEntity.status(HttpStatus.OK).body(voList);
    }

    // 강사 예비 블랙리스트 단건 조회
    @Operation(summary = "직원 - 강사 예비 블랙리스트 단건 세부 조회")
    @GetMapping("/tutor/reserved/{tutorcode}")
    public ResponseEntity<List<ResponseFindReservedBlacklistOneVO>> findTutorReservedBlacklist(
            @PathVariable("tutorcode") Long tutorCode
    ) {
        // 결국 예비 블랙리스트가 없어서 계산해서 가져와야함. -> Report에서 tutorcode에 해당하는 모든 Report 가져오고
        // -> 그 report 안에 있는 comment code를 통해서 comment도 가져와야함.
        List<BlacklistReportCommentDTO> dtoList = blacklistService.findBlacklistReportComment(tutorCode);

        List<ResponseFindReservedBlacklistOneVO> voList
                = blacklistMapper.fromBlacklistReportCommentDTOToResponseFindReservedBlacklistOneVO(dtoList);

        return ResponseEntity.status(HttpStatus.OK).body(voList);
    }

    // 멤버 예비 블랙리스트 -> 블랙리스트 "등록" 메서드
    @Operation(summary = "직원 - 예비 블랙리스트 블랙리스트 등록")
    @PostMapping("/{membercode}")
    public ResponseEntity<String> addMemberToBlacklist(
            @PathVariable("membercode") Long memberCode,
            @RequestBody RequestSaveBlacklistVO request
    ){
        BlacklistDTO blacklistDTO = new BlacklistDTO();
        blacklistDTO.setMemberCode(memberCode);
        blacklistDTO.setBlackReason(request.getBlackReason());

        blacklistService.addMemberToBlacklist(blacklistDTO);

        return ResponseEntity.status(HttpStatus.OK).body("블랙리스트 등록 성공");
    }
}
