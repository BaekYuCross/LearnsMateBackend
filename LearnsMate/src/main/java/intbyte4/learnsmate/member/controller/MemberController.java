package intbyte4.learnsmate.member.controller;

import intbyte4.learnsmate.common.mapper.MemberMapper;
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

    @PostMapping
    public ResponseEntity<String> saveMember(@RequestBody RequestSaveMemberVO request) {

        MemberDTO memberDTO = memberMapper.fromRequestSaveMemberVOtoDTO(request);

        memberService.saveMember(memberDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }
}
