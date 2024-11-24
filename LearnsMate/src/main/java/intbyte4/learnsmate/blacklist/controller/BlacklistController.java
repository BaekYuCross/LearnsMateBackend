package intbyte4.learnsmate.blacklist.controller;

import intbyte4.learnsmate.blacklist.domain.dto.*;
import intbyte4.learnsmate.blacklist.domain.vo.request.RequestFilterBlacklistMemberVO;
import intbyte4.learnsmate.blacklist.domain.vo.request.RequestSaveBlacklistVO;
import intbyte4.learnsmate.blacklist.domain.vo.response.ResponseFindBlacklistVO;
import intbyte4.learnsmate.blacklist.domain.vo.response.ResponseFindReservedBlacklistOneVO;
import intbyte4.learnsmate.blacklist.domain.vo.response.ResponseFindReservedStudentBlacklistVO;
import intbyte4.learnsmate.blacklist.domain.vo.response.ResponseFindReservedTutorBlacklistVO;
import intbyte4.learnsmate.blacklist.mapper.BlacklistMapper;
import intbyte4.learnsmate.blacklist.service.BlacklistService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.pagination.MemberPageResponse;
import intbyte4.learnsmate.member.domain.vo.response.ResponseFindMemberVO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/blacklist")
public class BlacklistController {

    private final BlacklistService blacklistService;
    private final BlacklistMapper blacklistMapper;

    // 1. 모든 학생 블랙리스트 조회
    @Operation(summary = "직원 - 학생 블랙리스트 전체 조회")
    @GetMapping("/student")
    public ResponseEntity<BlacklistPageResponse<ResponseFindBlacklistVO>> findAllStudentBlacklist(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size
    ) {
        BlacklistPageResponse<ResponseFindBlacklistVO> response
                = blacklistService.findAllBlacklistByMemberType(page, size, MemberType.STUDENT);

        return ResponseEntity.ok(response);
    }

    // 2. 모든 강사 블랙리스트 조회
    @Operation(summary = "직원 - 강사 블랙리스트 전체 조회")
    @GetMapping("/tutor")
    public ResponseEntity<BlacklistPageResponse<ResponseFindBlacklistVO>> findAllTutorBlacklist(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size
    ) {
        BlacklistPageResponse<ResponseFindBlacklistVO> response
                = blacklistService.findAllBlacklistByMemberType(page, size, MemberType.TUTOR);

        return ResponseEntity.ok(response);
    }

    // 3. 학생 블랙리스트 단건 조회
    @Operation(summary = "직원 - 학생 블랙리스트 단건 세부 조회")
    @GetMapping("/student/{blacklistcode}")
    public ResponseEntity<List<ResponseFindReservedBlacklistOneVO>> findStudentBlacklist(
            @PathVariable("blacklistcode") Long blacklistCode
    ) {
        // 보여줘야 하는것들
        // 학생 정보 + 신고정보 + 댓글정보 -> 예비 블랙리스트하고 똑같이 하면 될거같네
        List<BlacklistReportCommentDTO> dtoList = blacklistService.findBlacklistReportComment(blacklistCode, null);

        List<ResponseFindReservedBlacklistOneVO> voList
                = blacklistMapper.fromBlacklistReportCommentDTOToResponseFindReservedBlacklistOneVO(dtoList);

        return ResponseEntity.status(HttpStatus.OK).body(voList);
    }

    // 4. 강사 블랙리스트 단건 조회
    @Operation(summary = "직원 - 강사 블랙리스트 단건 세부 조회")
    @GetMapping("/tutor/{blacklistcode}")
    public ResponseEntity<List<ResponseFindReservedBlacklistOneVO>> findTutorBlacklist(
            @PathVariable("blacklistcode") Long blacklistCode
    ) {
        List<BlacklistReportCommentDTO> dtoList = blacklistService.findBlacklistReportComment(blacklistCode, null);

        List<ResponseFindReservedBlacklistOneVO> voList
                = blacklistMapper.fromBlacklistReportCommentDTOToResponseFindReservedBlacklistOneVO(dtoList);

        return ResponseEntity.status(HttpStatus.OK).body(voList);
    }

    // 직원 - 예비 블랙리스트 전체 조회(학생)
    @Operation(summary = "직원 - 학생 예비 블랙리스트 전체 조회")
    @GetMapping("/student/reserved")
    public ResponseEntity<ReservedBlacklistPageResponse<ResponseFindReservedStudentBlacklistVO>> findAllStudentReservedBlacklist(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size
    ) {

        ReservedBlacklistPageResponse<ResponseFindReservedStudentBlacklistVO> response
                = (ReservedBlacklistPageResponse<ResponseFindReservedStudentBlacklistVO>)
                blacklistService.findAllReservedBlacklistByMemberType(page, size, MemberType.STUDENT);

        return ResponseEntity.ok(response);
    }

    // 직원 - 예비 블랙리스트 전체 조회(강사)
    @Operation(summary = "직원 - 강사 예비 블랙리스트 전체 조회")
    @GetMapping("/tutor/reserved")
    public ResponseEntity<ReservedBlacklistPageResponse<ResponseFindReservedTutorBlacklistVO>> findAllTutorReservedBlacklist(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ){

        ReservedBlacklistPageResponse<ResponseFindReservedTutorBlacklistVO> response
                = (ReservedBlacklistPageResponse<ResponseFindReservedTutorBlacklistVO>)
                blacklistService.findAllReservedBlacklistByMemberType(page, size, MemberType.TUTOR);

        return ResponseEntity.ok(response);
    }

    // 학생 예비 블랙리스트 단건 조회
    @Operation(summary = "직원 - 학생 예비 블랙리스트 단건 세부 조회")
    @GetMapping("/student/reserved/{studentcode}")
    public ResponseEntity<List<ResponseFindReservedBlacklistOneVO>> findStudentReservedBlacklist(
            @PathVariable("studentcode") Long studentCode
    ) {
        // 결국 예비 블랙리스트가 없어서 계산해서 가져와야함. -> Report에서 tutorcode에 해당하는 모든 Report 가져오고
        // -> 그 report 안에 있는 comment code를 통해서 comment도 가져와야함.
        List<BlacklistReportCommentDTO> dtoList = blacklistService.findBlacklistReportComment(null, studentCode);

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
        List<BlacklistReportCommentDTO> dtoList = blacklistService.findBlacklistReportComment(null, tutorCode);

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

        log.info("MemberCode: {}", memberCode);
        log.info("Request Body: {}", request);
        BlacklistDTO blacklistDTO = new BlacklistDTO();
        blacklistDTO.setMemberCode(memberCode);
        blacklistDTO.setBlackReason(request.getBlackReason());

        blacklistService.addMemberToBlacklist(blacklistDTO);
        return ResponseEntity.status(HttpStatus.OK).body("블랙리스트 등록 성공");
    }

    @Operation(summary = "학생 - 블랙리스트 필터링 기능 추가")
    @PostMapping("/filter/student")
    public ResponseEntity<BlacklistPageResponse<ResponseFindBlacklistVO>> filterBlackStudent(
            @RequestBody RequestFilterBlacklistMemberVO vo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ){
        log.info(vo.toString());
        BlacklistFilterRequestDTO dto = blacklistMapper.fromFilterMemberVOtoFilterMemberDTO(vo);
        dto.setMemberType(MemberType.STUDENT);

        BlacklistPageResponse<ResponseFindBlacklistVO> response = blacklistService.filterBlacklistMember(dto, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "강사 - 블랙리스트 필터링 기능 추가")
    @PostMapping("/filter/tutor")
    public ResponseEntity<?> filterBlackTutor(
            @RequestBody RequestFilterBlacklistMemberVO vo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ){
        log.info(vo.toString());
        BlacklistFilterRequestDTO dto = blacklistMapper.fromFilterMemberVOtoFilterMemberDTO(vo);
        dto.setMemberType(MemberType.TUTOR);

        BlacklistPageResponse<ResponseFindBlacklistVO> response = blacklistService.filterBlacklistMember(dto, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
