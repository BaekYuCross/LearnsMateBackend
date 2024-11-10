package intbyte4.learnsmate.admin.controller;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.vo.request.RequestEditAdminVO;
import intbyte4.learnsmate.admin.domain.vo.response.ResponseEditAdminVO;
import intbyte4.learnsmate.admin.domain.vo.response.ResponseFindAdminVO;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
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
}
