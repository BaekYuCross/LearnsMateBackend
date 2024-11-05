package intbyte4.learnsmate.admin.service;


import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.repository.AdminRepository;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public AdminDTO findByAdminCode(Long adminCode) {
        Admin admin = adminRepository.findById(adminCode).orElseThrow(()-> new CommonException(StatusEnum.ADMIN_NOT_FOUND));

        return admin.convertToDTO();
    }
}
