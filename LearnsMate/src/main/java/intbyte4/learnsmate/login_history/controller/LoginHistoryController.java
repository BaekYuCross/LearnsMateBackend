package intbyte4.learnsmate.login_history.controller;

import intbyte4.learnsmate.login_history.domain.dto.LoginHistoryDTO;
import intbyte4.learnsmate.login_history.domain.vo.response.ResponseFindLoginHistoryVO;
import intbyte4.learnsmate.login_history.mapper.LoginHistoryMapper;
import intbyte4.learnsmate.login_history.service.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/loginhistory")
public class LoginHistoryController {

    private final LoginHistoryService loginHistoryService;
    private final LoginHistoryMapper loginHistoryMapper;

    // 1. 모든 Login History 기록 조회
    @GetMapping
    public ResponseEntity<List<ResponseFindLoginHistoryVO>> findAll(){

        List<LoginHistoryDTO> loginHistoryDTOList = loginHistoryService.findAllLoginHistory();

        List<ResponseFindLoginHistoryVO> responseList =
                loginHistoryMapper.fromLoginHistoryDTOToResponseFindLoginHistoryVO(loginHistoryDTOList);

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    // 2. 특정 Login History 기록 조회
    @GetMapping("/{loginhistorycode}")
    public ResponseEntity<ResponseFindLoginHistoryVO> findByLoginHistoryCode(@PathVariable("loginhistorycode") Long loginHistoryCode) {
        LoginHistoryDTO loginHistoryDTO = loginHistoryService.findById(loginHistoryCode);

        return ResponseEntity.status(HttpStatus.OK)
                .body(loginHistoryMapper.fromLoginHistoryDTOToResponseFindLoginHistoryVO(loginHistoryDTO));
    }

    // 3. 특정 멤버의 모든 로그인 기록 확인
    @GetMapping("/member/login/{membercode}")
    public ResponseEntity<List<ResponseFindLoginHistoryVO>> findAllByMemberCode(@PathVariable("membercode") Long memberCode) {

        List<LoginHistoryDTO> loginHistoryDTOList = loginHistoryService.findAllLoginHistoryByMemberCode(memberCode);

        return ResponseEntity.status(HttpStatus.OK)
                .body(loginHistoryMapper.fromLoginHistoryDTOToResponseFindLoginHistoryVO(loginHistoryDTOList));
    }

    // 4. 특정 멤버의 모든 로그아웃 기록 확인
    @GetMapping("/member/logout/{membercode}")
    public ResponseEntity<?> findAllLogoutHistoryByMemberCode(@PathVariable Long membercode){
        return null;
    }



}
