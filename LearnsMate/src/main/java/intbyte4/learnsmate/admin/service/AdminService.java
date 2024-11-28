package intbyte4.learnsmate.admin.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.vo.request.RequestResetPasswordVO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AdminService extends UserDetailsService {
    AdminDTO findByAdminCode(Long adminCode);

    AdminDTO updateAdmin(AdminDTO editAdminVO);

    AdminDTO findUserByEmail(String adminEmail);

    void resetPassword(RequestResetPasswordVO request);
}
