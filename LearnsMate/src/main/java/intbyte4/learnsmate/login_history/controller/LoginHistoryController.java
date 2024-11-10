package intbyte4.learnsmate.login_history.controller;

import intbyte4.learnsmate.login_history.domain.dto.LoginHistoryDTO;
import intbyte4.learnsmate.login_history.domain.vo.request.RequestSaveLoginHistoryVO;
import intbyte4.learnsmate.login_history.domain.vo.response.ResponseFindLoginHistoryVO;
import intbyte4.learnsmate.login_history.mapper.LoginHistoryMapper;
import intbyte4.learnsmate.login_history.service.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    // 3. 특정 멤버의 모든 로그인 내역 확인
    @GetMapping("/member/{membercode}")
    public ResponseEntity<List<ResponseFindLoginHistoryVO>> findAllByMemberCode(@PathVariable("membercode") Long memberCode) {

        List<LoginHistoryDTO> loginHistoryDTOList = loginHistoryService.findAllLoginHistoryByMemberCode(memberCode);

        return ResponseEntity.status(HttpStatus.OK)
                .body(loginHistoryMapper.fromLoginHistoryDTOToResponseFindLoginHistoryVO(loginHistoryDTOList));
    }

    // 4. 로그인 내역에 추가 -> 어떤 회원이 로그인 -> 로그인 시간을 알고있음. -> 로그아웃 시간만 비어있는 상태로 진행하기 -> 로그인 내역 번호를 알고있어야함. -> 반환해주기
    @PostMapping("/member")
    public ResponseEntity<Long> saveLoginHisotry(
            @RequestBody RequestSaveLoginHistoryVO request){

        Long loginHistoryCode =
                loginHistoryService.saveLoginHistory(loginHistoryMapper.fromRequestSaveLoginHistoryVOtoLoginHistoryDTO(request));

        // 로그인 내역 코드 반환해주기
        return ResponseEntity.status(HttpStatus.OK)
                .body(loginHistoryCode);
    }

    // 5. 로그아웃 하는 경우 내역에 추가해주기
    @PatchMapping("/member/{loginhistorycode}")
    public ResponseEntity<String> saveLogoutTime(@PathVariable("loginhistorycode") Long loginHistoryCode){
        loginHistoryService.saveLogoutTime(loginHistoryCode);

        return ResponseEntity.status(HttpStatus.OK).body("수정 완료");
    }



}
