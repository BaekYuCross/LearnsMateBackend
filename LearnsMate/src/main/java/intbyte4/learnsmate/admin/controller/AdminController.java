package intbyte4.learnsmate.admin.controller;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.CustomUserDetails;
import intbyte4.learnsmate.admin.domain.vo.request.RequestEditAdminVO;
import intbyte4.learnsmate.admin.domain.vo.response.ResponseEditAdminVO;
import intbyte4.learnsmate.admin.domain.vo.response.ResponseFindAdminVO;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @Operation(summary = "직원 단건 조회")
    @GetMapping("/{adminCode}")
    public ResponseEntity<ResponseFindAdminVO> getAdmin(@PathVariable Long adminCode) {
        AdminDTO updatedAdmin = adminService.findByAdminCode(adminCode);
        return ResponseEntity.status(HttpStatus.OK).body(adminMapper.fromDtoToFindResponseVO(updatedAdmin));
    }

    @Operation(summary = "직원 정보 수정")
    @PatchMapping("/{adminCode}")
    public ResponseEntity<ResponseEditAdminVO> updateAdmin(@PathVariable Long adminCode,
                                                           @RequestBody RequestEditAdminVO editAdminVO) {
        AdminDTO updatedAdmin = adminService.updateAdmin(adminCode, adminMapper.fromVoToDto(editAdminVO));
        return ResponseEntity.status(HttpStatus.OK).body(adminMapper.fromDtoToEditResponseVO(updatedAdmin));
    }

    // @AuthenticationPrincipal을 활용 -> CustomUserDetails에서 사용자 정보를 추출
    // 인증 성공 시 사용자 이름과 권한을 상태에 저장 -> Pinia 로 이름과 권한 정보 넘어감 (loginState.js 롹인 바람)
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> checkAuthStatus(@AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("GET /admin/status 요청 도착");
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("name", userDetails.getUserDTO().getAdminName()); // 관리자 이름
        response.put("roles", userDetails.getAuthorities()); // 권한 정보

        return ResponseEntity.ok(response);
    }
}
