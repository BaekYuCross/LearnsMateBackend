package intbyte4.learnsmate.member.controller;

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
@RequestMapping("member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseFindMemberVO>> findAllMember() {

        List<MemberDTO> memberDTOList = memberService.findAllMember();

        // DTO 리스트를 VO 리스트로 변환
        List<ResponseFindMemberVO> responseVOList = memberDTOList.stream()
                .map(ResponseFindMemberVO::fromDTO)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(responseVOList);
    }

    @PostMapping
    public ResponseEntity<String> saveMember(@RequestBody RequestSaveMemberVO request) {

        MemberDTO memberDTO = new MemberDTO().insertMemberRequest(request);

        memberService.saveMember(memberDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }
}
