package intbyte4.learnsmate.admin.service;


import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.domain.entity.CustomUserDetails;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.repository.AdminRepository;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AdminMapper adminMapper;

    @Override
    public AdminDTO findByAdminCode(Long adminCode) {
        Admin admin = adminRepository.findById(adminCode).orElseThrow(()-> new CommonException(StatusEnum.ADMIN_NOT_FOUND));
        return adminMapper.toDTO(admin);
    }

    @Override
    public AdminDTO updateAdmin(AdminDTO adminDTO) {
        Admin admin = adminRepository.findById(adminDTO.getAdminCode())
                .orElseThrow(() -> new CommonException(StatusEnum.ADMIN_NOT_FOUND));
        admin.toUpdate(adminDTO);
        Admin updatedAdmin = adminRepository.save(admin);
        return adminMapper.toDTO(updatedAdmin);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin admin = adminRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> new CommonException(StatusEnum.ADMIN_NOT_FOUND));

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        // 기본적으로 ROLE_ADMIN 권한 추가
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (!admin.getAdminStatus()) {
            throw new CommonException(StatusEnum.ADMIN_NOT_FOUND);
        }

        AdminDTO adminDTO = adminMapper.toDTO(admin);
        return new CustomUserDetails(adminDTO, grantedAuthorities, true, true, true, true);
    }


    @Override
    public AdminDTO findUserByEmail(String adminEmail) {
        Admin admin = adminRepository.findByAdminEmail(adminEmail);
        if (admin == null) {
            throw new CommonException(StatusEnum.ADMIN_NOT_FOUND);
        }
        return adminMapper.toDTO(admin);
    }

    @Override
    public void resetPassword(AdminDTO request) {

        Admin admin = adminRepository.findByAdminEmail(request.getAdminEmail());
        if (admin == null) {
            throw new CommonException(StatusEnum.ADMIN_NOT_FOUND);
        }

        // 비밀번호 bCrypt 암호화.
        admin.setAdminPassword(bCryptPasswordEncoder.encode(request.getAdminPassword()));

        Admin updatedAdmin = adminRepository.save(admin);
        adminMapper.toDTO(updatedAdmin);
    }

}

