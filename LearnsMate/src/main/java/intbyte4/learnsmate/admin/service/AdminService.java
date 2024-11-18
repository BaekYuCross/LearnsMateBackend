package intbyte4.learnsmate.admin.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AdminService extends UserDetailsService {
    AdminDTO findByAdminCode(Long adminCode);

    AdminDTO updateAdmin(Long adminCode, AdminDTO editAdminVO);
}
