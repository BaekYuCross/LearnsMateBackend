package intbyte4.learnsmate.admin.service;


import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.repository.AdminRepository;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    @Override
    public AdminDTO findByAdminCode(Long adminCode) {
        Admin admin = adminRepository.findById(adminCode).orElseThrow(()-> new CommonException(StatusEnum.ADMIN_NOT_FOUND));
        return adminMapper.toDTO(admin);
    }

    @Override
    public AdminDTO updateAdmin(Long adminCode, AdminDTO adminDTO) {
        Admin admin = adminRepository.findById(adminCode)
                .orElseThrow(() -> new CommonException(StatusEnum.ADMIN_NOT_FOUND));
        admin.toUpdate(adminDTO);
        Admin updatedAdmin = adminRepository.save(admin);
        return adminMapper.toDTO(updatedAdmin);
    }
}
