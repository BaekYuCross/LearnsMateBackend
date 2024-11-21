package intbyte4.learnsmate.member.controller;

import intbyte4.learnsmate.member.domain.dto.*;
import intbyte4.learnsmate.member.domain.vo.request.RequestEditMemberVO;
import intbyte4.learnsmate.member.domain.vo.request.RequestFilterMembertVO;
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

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MemberFacade memberFacade;
    private final MemberMapper memberMapper;

    // 1. 모든 학생 조회(member_flag가 true인 사람 + member_type이 STUDENT)
    @GetMapping("/students")
    public ResponseEntity<MemberPageResponse<ResponseFindMemberVO>> findAllStudent(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {

        MemberPageResponse<ResponseFindMemberVO> response
                = memberService.findAllMemberByMemberType(page, size, MemberType.STUDENT);
        return ResponseEntity.ok(response);
    }

    // 1-2. 모든 강사 조회(member_flag가 true + member_type이 TUTOR)
    @GetMapping("/tutors")
    public ResponseEntity<MemberPageResponse<ResponseFindMemberVO>> findAllTutor(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {

        MemberPageResponse<ResponseFindMemberVO> response
                = memberService.findAllMemberByMemberType(page, size, MemberType.TUTOR);

        return ResponseEntity.ok(response);
    }

    // 2-1. 학생 단건 조회(member flag가 true + member_type이 STUDENT)
    @GetMapping("/student/{studentcode}")
    public ResponseEntity<ResponseFindStudentDetailVO> findStudentByStudentCode(@PathVariable("studentcode") Long studentCode) {

        FindSingleStudentDTO dto = memberFacade.findStudentByStudentCode(studentCode);

        ResponseFindStudentDetailVO vo
                = memberMapper.fromFindSingleStudentDTOtoResponseFindStudentDetailVO(dto);

        return ResponseEntity.status(HttpStatus.OK).body(vo);
    }

    // 2-2. 강사 단건 조회(member flag가 true + member_type이 TUTOR)
    @GetMapping("/tutor/{tutorcode}")
    public ResponseEntity<ResponseFindTutorDetailVO> findTutorByTutorCode(@PathVariable("tutorcode") Long tutorCode) {

        FindSingleTutorDTO dto = memberFacade.findTutorByTutorCode(tutorCode);

        ResponseFindTutorDetailVO vo
                = memberMapper.fromFindSingleTutorDTOtoResponseFindTutorDetailVO(dto);

        return ResponseEntity.status(HttpStatus.OK).body(vo);
    }

    // 회원가입시 멤버 저장하는 컨트롤러 메서드
    @PostMapping
    public ResponseEntity<String> saveMember(@RequestBody RequestSaveMemberVO request) {

        MemberDTO memberDTO = memberMapper.fromRequestSaveMemberVOtoMemberDTO(request);

        memberService.saveMember(memberDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    // 회원 수정하는 컨트롤러 메서드
    @PatchMapping("/{membercode}")
    public ResponseEntity<String> editMember(@PathVariable("membercode") Long memberCode, @RequestBody RequestEditMemberVO request) {
        // request에는 membercode가 없음.
        MemberDTO memberDTO = memberMapper.fromRequestEditMemberVOToMemberDTO(request);
        memberDTO.setMemberCode(memberCode);

        memberService.editMember(memberDTO);

        return ResponseEntity.status(HttpStatus.OK).body("수정 성공");
    }

    // 회원 삭제하는 컨트롤러 메서드
    @PatchMapping("/delete/{membercode}")
    public ResponseEntity<String> deleteMember(@PathVariable("membercode") Long memberCode) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberCode(memberCode);

        memberService.deleteMember(memberCode);

        return ResponseEntity.status(HttpStatus.OK).body("삭제 성공");
    }

    @Operation(summary = "직원 - 학생 필터링 검색")
    @PostMapping("/filter/student")
    public ResponseEntity<MemberPageResponse<ResponseFindMemberVO>> findStudentByFilter(
            @RequestBody RequestFilterMembertVO request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        MemberFilterRequestDTO dto =
                memberMapper.fromRequestFilterVOtoMemberFilterRequestDTO(request);

        dto.setMemberType(MemberType.STUDENT);

        MemberPageResponse<ResponseFindMemberVO> response = memberService.filterStudent(dto, page, size);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "직원 - 강사 필터링 검색")
    @PostMapping("/filter/tutor")
    public ResponseEntity<?> findTutorByFilter(
            @RequestBody RequestFilterMembertVO request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        MemberFilterRequestDTO dto =
                memberMapper.fromRequestFilterVOtoMemberFilterRequestDTO(request);

        dto.setMemberType(MemberType.TUTOR);

        MemberPageResponse<ResponseFindMemberVO> response = memberService.filterTutor(dto, page, size);

        return ResponseEntity.ok(response);
    }
}
