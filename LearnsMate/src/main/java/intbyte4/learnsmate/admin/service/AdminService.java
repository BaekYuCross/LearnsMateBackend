package intbyte4.learnsmate.admin.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;

public interface AdminService {
    AdminDTO findByAdminCode(Long adminCode);

    AdminDTO updateAdmin(Long adminCode, AdminDTO editAdminVO);
}
