package intbyte4.learnsmate.client.controller;


import intbyte4.learnsmate.client.service.ClientService;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.vo.request.RequestLoginVO;
import intbyte4.learnsmate.member.domain.vo.request.RequestSaveMemberVO;
import intbyte4.learnsmate.member.domain.vo.response.ResponseLoginVO;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final MemberMapper memberMapper;

    @Operation(summary = "회원 - 회원 가입")
    @PostMapping
    public ResponseEntity<String> saveMember(@RequestBody RequestSaveMemberVO request) {
        MemberDTO memberDTO = memberMapper.fromRequestSaveMemberVOtoMemberDTO(request);

        clientService.saveMember(memberDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    @Operation(summary = "회원 - 로그인")
    @PostMapping("/login")
    public ResponseEntity<ResponseLoginVO> memberLogin(@RequestBody RequestLoginVO request){
        MemberDTO memberDTO = MemberDTO.builder()
                .memberEmail(request.getMemberEmail())
                .memberPassword(request.getMemberPassword())
                .build();

        MemberDTO findMemberDTO = clientService.loginMember(memberDTO);
        ResponseLoginVO vo = memberMapper.fromMemberDTOtoResponseLoginVO(findMemberDTO);

        return ResponseEntity.ok(vo);
    }
}
