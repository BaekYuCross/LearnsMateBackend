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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> new CommonException(StatusEnum.ADMIN_NOT_FOUND));

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

//        switch (admin.getUserType()) {
//            case "Employee" -> grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
//            case "Employer" -> grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYER"));
//            case "Admin" -> {
//                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYER"));
//                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
//            }
//        }
        if (!admin.getAdminStatus()) throw new CommonException(StatusEnum.ADMIN_NOT_FOUND);
        // 고객의 학생과 강사도 가져와서 예외처리랑 역할 분배 해주기

        AdminDTO adminDTO = adminMapper.toDTO(admin);

        return new CustomUserDetails(adminDTO, grantedAuthorities, true, true, true, true);
    }
}

