package intbyte4.learnsmate.admin.controller;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.dto.ResponseEmailDTO;
import intbyte4.learnsmate.admin.domain.entity.CustomUserDetails;
import intbyte4.learnsmate.admin.domain.vo.request.AdminEmailVerificationVO;
import intbyte4.learnsmate.admin.domain.vo.request.EmailVerificationVO;
import intbyte4.learnsmate.admin.domain.vo.request.RequestEditAdminVO;
import intbyte4.learnsmate.admin.domain.vo.request.RequestResetPasswordVO;
import intbyte4.learnsmate.admin.domain.vo.response.ResponseEditAdminVO;
import intbyte4.learnsmate.admin.domain.vo.response.ResponseEmailMessageVO;
import intbyte4.learnsmate.admin.domain.vo.response.ResponseFindAdminVO;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.admin.service.EmailService;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final AdminMapper adminMapper;
    private final EmailService emailService;

    @Operation(summary = "직원 단건 조회")
    @GetMapping("/{adminCode}")
    public ResponseEntity<ResponseFindAdminVO> getAdmin(@PathVariable Long adminCode) {
        AdminDTO updatedAdmin = adminService.findByAdminCode(adminCode);
        return ResponseEntity.status(HttpStatus.OK).body(adminMapper.fromDtoToFindResponseVO(updatedAdmin));
    }

    @Operation(summary = "직원 정보 수정")
    @PatchMapping("/password")
    public ResponseEntity<ResponseEditAdminVO> updateAdmin(@RequestBody RequestEditAdminVO editAdminVO) {
        AdminDTO updatedAdmin = adminService.updateAdmin(adminMapper.fromVoToDto(editAdminVO));
        return ResponseEntity.status(HttpStatus.OK).body(adminMapper.fromDtoToEditResponseVO(updatedAdmin));
    }

    // @AuthenticationPrincipal을 활용 -> CustomUserDetails에서 사용자 정보를 추출
    // 인증 성공 시 사용자 이름과 권한을 상태에 저장 -> Pinia 로 이름과 권한 정보 넘어감 (loginState.js 롹인 바람)
    @Operation(summary = "직원 정보 조회")
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> checkAuthStatus(@AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("GET /admin/status 요청 도착");
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("name", userDetails.getUserDTO().getAdminName()); // 관리자 이름
        response.put("code", userDetails.getUserDTO().getAdminCode()); // 관리자 사번
        response.put("adminDepartment", userDetails.getUserDTO().getAdminDepartment()); // 관리자 부서
        response.put("roles", userDetails.getAuthorities()); // 권한 정보

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "직원 로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        log.info("POST /admin/logout 요청 도착");
        // 쿠키 삭제 명령
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // 쿠키 만료 처리
        response.addCookie(cookie);

        // 필요 시 블랙리스트로 JWT 관리
        log.info("로그아웃 성공");
        return ResponseEntity.ok().body("로그아웃 성공");
    }

    // 인증버튼
    @Operation(summary = "직원 비밀번호 재설정시 이메일 전송")
    @PostMapping("/verification-email/password")
    public ResponseEmailDTO<?> sendVerificationEmailPassword(@RequestBody @Validated AdminEmailVerificationVO request) {

        // 입력한 사번 코드와 이메일의 정보 검증
        AdminDTO adminByEmail = adminService.findUserByEmail(request.getEmail());

        if (adminByEmail == null || !adminByEmail.getAdminCode().equals(request.getAdminCode())) {
            throw new CommonException(StatusEnum.EMAIL_NOT_FOUND);
        }
        // 이메일로 인증번호 전송
        return getResponseEmailDTO(request);
    }

    private ResponseEmailDTO<?> getResponseEmailDTO(AdminEmailVerificationVO request) {
        // 이메일로 인증번호 전송
        try {
            emailService.sendVerificationEmail(request.getEmail());

            ResponseEmailMessageVO responseEmailMessageVO =new ResponseEmailMessageVO();
            responseEmailMessageVO.setMessage("인증 코드가 이메일로 전송되었습니다.");
            return ResponseEmailDTO.ok(responseEmailMessageVO);
        } catch (Exception e) {
            return ResponseEmailDTO.fail(new CommonException(StatusEnum.INTERNAL_SERVER_ERROR));
        }
    }


    // 다음버튼
    @Operation(summary = "비번 재설정 전 이메일 인증번호 검증")
    @PostMapping("/verification-email/confirmation")
    public ResponseEmailDTO<?> verifyEmail(@RequestBody @Validated EmailVerificationVO request) {
        boolean isVerified = emailService.verifyCode(request.getEmail(), request.getCode());

        ResponseEmailMessageVO responseEmailMessageVO =new ResponseEmailMessageVO();
        responseEmailMessageVO.setMessage("이메일 인증이 완료되었습니다.");
        if (isVerified) {
            return ResponseEmailDTO.ok(responseEmailMessageVO);
        } else {
            return ResponseEmailDTO.fail(new CommonException(StatusEnum.INVALID_VERIFICATION_CODE));
        }
    }

    // 다음버튼 누를 시, 인증성공하면 ? -> 비번 재설정 칸 생성 -> 완료 버튼
    @Operation(summary = "비밀번호 재설정")
    @PostMapping("/password")
    public ResponseEntity<String> resetPassword(@RequestBody RequestResetPasswordVO request) {
        log.info("POST /admin/password 요청 도착: email={}", request.getUserEmail());
        log.info("POST /admin/password 요청 도착: email={}", request.getUserPassword());

        adminService.resetPassword(request);

        log.info("비밀번호가 성공적으로 재설정되었습니다.");
        return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.");
    }
}
