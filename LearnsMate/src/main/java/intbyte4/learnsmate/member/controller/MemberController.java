package intbyte4.learnsmate.member.controller;

import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategoryEnum;
import intbyte4.learnsmate.member.domain.dto.*;
import intbyte4.learnsmate.member.domain.pagination.MemberPageResponse;
import intbyte4.learnsmate.member.domain.vo.request.CategoryRatioFilterRequest;
import intbyte4.learnsmate.member.domain.vo.request.RequestEditMemberVO;
import intbyte4.learnsmate.member.domain.vo.request.RequestFilterMemberVO;
import intbyte4.learnsmate.member.domain.vo.response.ResponseFindStudentDetailVO;
import intbyte4.learnsmate.member.domain.vo.response.ResponseFindTutorDetailVO;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.vo.request.RequestSaveMemberVO;
import intbyte4.learnsmate.member.domain.vo.response.ResponseFindMemberVO;
import intbyte4.learnsmate.member.service.MemberFacade;
import intbyte4.learnsmate.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MemberFacade memberFacade;
    private final MemberMapper memberMapper;

    @Operation(summary = "직원 - 학생 전체 조회")
    @GetMapping("/students")
    public ResponseEntity<MemberPageResponse<ResponseFindMemberVO>> findAllStudent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        MemberPageResponse<ResponseFindMemberVO> response = memberFacade.findAllMemberByMemberType(page, size, MemberType.STUDENT);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "직원 - 강사 전체 조회")
    @GetMapping("/tutors")
    public ResponseEntity<MemberPageResponse<ResponseFindMemberVO>> findAllTutor(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        MemberPageResponse<ResponseFindMemberVO> response = memberService.findAllMemberByMemberType(page, size, MemberType.TUTOR);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "직원 - 학생 단 건 조회")
    @GetMapping("/student/{studentCode}")
    public ResponseEntity<ResponseFindStudentDetailVO> findStudentByStudentCode(@PathVariable("studentCode") Long studentCode) {
        FindSingleStudentDTO dto = memberFacade.findStudentByStudentCode(studentCode);

        ResponseFindStudentDetailVO vo = memberMapper.fromFindSingleStudentDTOtoResponseFindStudentDetailVO(dto);

        return ResponseEntity.status(HttpStatus.OK).body(vo);
    }

    @Operation(summary = "직원 - 강사 단 건 조회")
    @GetMapping("/tutor/{tutorCode}")
    public ResponseEntity<ResponseFindTutorDetailVO> findTutorByTutorCode(@PathVariable("tutorCode") Long tutorCode) {

        FindSingleTutorDTO dto = memberFacade.findTutorByTutorCode(tutorCode);

        ResponseFindTutorDetailVO vo = memberMapper.fromFindSingleTutorDTOtoResponseFindTutorDetailVO(dto);

        return ResponseEntity.status(HttpStatus.OK).body(vo);
    }

    @Operation(summary = "직원 - 회원 등록")
    @PostMapping
    public ResponseEntity<String> saveMember(@RequestBody RequestSaveMemberVO request) {
        MemberDTO memberDTO = memberMapper.fromRequestSaveMemberVOtoMemberDTO(request);

        memberService.saveMember(memberDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    @Operation(summary = "직원 - 회원 수정")
    @PatchMapping("/{memberCode}")
    public ResponseEntity<String> editMember(@PathVariable("memberCode") Long memberCode, @RequestBody RequestEditMemberVO request) {
        MemberDTO memberDTO = memberMapper.fromRequestEditMemberVOToMemberDTO(request);
        memberDTO.setMemberCode(memberCode);

        memberService.editMember(memberDTO);

        return ResponseEntity.status(HttpStatus.OK).body("수정 성공");
    }

    @Operation(summary = "직원 - 회원 삭제")
    @PatchMapping("/delete/{memberCode}")
    public ResponseEntity<String> deleteMember(@PathVariable("memberCode") Long memberCode) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberCode(memberCode);

        memberService.deleteMember(memberCode);

        return ResponseEntity.status(HttpStatus.OK).body("삭제 성공");
    }

    @Operation(summary = "직원 - 학생 필터링 검색")
    @PostMapping("/filter/student")
    public ResponseEntity<MemberPageResponse<ResponseFindMemberVO>> findStudentByFilter(
            @RequestBody RequestFilterMemberVO request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        log.info("vo는 :{}", request );
        MemberFilterRequestDTO dto =
                memberMapper.fromRequestFilterVOtoMemberFilterRequestDTO(request);

        dto.setMemberType(MemberType.STUDENT);

        MemberPageResponse<ResponseFindMemberVO> response = memberService.filterStudent(dto, page, size);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "직원 - 강사 필터링 검색")
    @PostMapping("/filter/tutor")
    public ResponseEntity<?> findTutorByFilter(
            @RequestBody RequestFilterMemberVO request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        MemberFilterRequestDTO dto =
                memberMapper.fromRequestFilterVOtoMemberFilterRequestDTO(request);

        dto.setMemberType(MemberType.TUTOR);

        MemberPageResponse<ResponseFindMemberVO> response = memberService.filterTutor(dto, page, size);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "총 기간 카테고리 별 강의를 가진 학생 수 비율 조회")
    @GetMapping("/category-ratio")
    public ResponseEntity<List<CategoryStatDTO>> getCategoryRatio() {
        List<CategoryCountDTO> categoryCounts = memberFacade.getCategoryCounts();
        List<CategoryStatDTO> stats = memberFacade.calculateCategoryStats(categoryCounts);
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "특정 기간 카테고리 별 강의를 가진 학생 수 비율 조회")
    @PostMapping("/category-ratio/filter")
    public ResponseEntity<List<CategoryStatDTO>> getFilteredCategoryRatioWithPercentage(@RequestBody CategoryRatioFilterRequest request) {
        List<CategoryCountDTO> categoryCounts = memberFacade.getFilteredCategoryCounts(request.getStartDate(), request.getEndDate());
        List<CategoryStatDTO> stats = memberFacade.calculateCategoryStats(categoryCounts);
        return ResponseEntity.ok(stats);
    }
}
