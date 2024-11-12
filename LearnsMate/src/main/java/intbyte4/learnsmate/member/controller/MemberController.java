package intbyte4.learnsmate.member.controller;

import intbyte4.learnsmate.member.domain.dto.MemberIssueCouponDTO;
import intbyte4.learnsmate.member.domain.vo.request.RequestEditMemberVO;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.vo.request.RequestSaveMemberVO;
import intbyte4.learnsmate.member.domain.vo.response.ResponseFindMemberVO;
import intbyte4.learnsmate.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @Autowired
    public MemberController(MemberService memberService, MemberMapper memberMapper) {
        this.memberService = memberService;
        this.memberMapper = memberMapper;
    }

    // 1. 모든 학생 조회(member_flag가 true인 사람 + member_type이 STUDENT)
    @GetMapping("/students")
    public ResponseEntity<List<ResponseFindMemberVO>> findAllStudent() {

        List<MemberDTO> memberDTOList = memberService.findAllMemberByMemberType(MemberType.STUDENT);

        // DTO 리스트를 VO 리스트로 변환
        List<ResponseFindMemberVO> responseVOList = memberDTOList.stream()
                .map(memberMapper::fromMemberDTOtoResponseFindMemberVO)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(responseVOList);
    }

    // 1-2. 모든 강사 조회(member_flag가 true + member_type이 TUTOR)
    @GetMapping("/tutors")
    public ResponseEntity<List<ResponseFindMemberVO>> findAllTutor() {

        List<MemberDTO> memberDTOList = memberService.findAllMemberByMemberType(MemberType.TUTOR);

        // DTO 리스트를 VO 리스트로 변환
        List<ResponseFindMemberVO> responseVOList = memberDTOList.stream()
                .map(memberMapper::fromMemberDTOtoResponseFindMemberVO)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(responseVOList);
    }

    //////////////////// 쿠폰, 강의, voc 서비스 메서드 추가 필요 ///////////////////////////
    // 2-1. 학생 단건 조회(member flag가 true + member_type이 STUDENT)
    @GetMapping("/student/{studentcode}")
    public ResponseEntity<ResponseFindMemberVO> findStudentByStudentCode(@PathVariable("studentcode") Long memberCode) {

        MemberDTO memberDTO = memberService.findMemberByMemberCode(memberCode, MemberType.STUDENT);
        MemberIssueCouponDTO memberIssueCouponDTO = memberService.memberIssueCoupon(memberCode);

        return ResponseEntity.status(HttpStatus.OK).body(memberMapper.fromMemberDTOtoResponseFindMemberVO(memberDTO));
    }

    // 2-2. 강사 단건 조회(member flag가 true + member_type이 TUTOR)
    @GetMapping("/tutor/{tutorcoe}")
    public ResponseEntity<ResponseFindMemberVO> findTutorByTutorCode(@PathVariable("tutorcode") Long memberCode) {

        MemberDTO memberDTO = memberService.findMemberByMemberCode(memberCode, MemberType.TUTOR);
        MemberIssueCouponDTO memberIssueCouponDTO = memberService.memberIssueCoupon(memberCode);

        return ResponseEntity.status(HttpStatus.OK).body(memberMapper.fromMemberDTOtoResponseFindMemberVO(memberDTO));
    }
    ////////////////////////////////////////////////////////////

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
}
