package intbyte4.learnsmate.admin.mapper;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

    // Admin 엔티티 -> AdminDTO 변환
    public AdminDTO toDTO(Admin admin) {

        return AdminDTO.builder()
                .adminCode(admin.getAdminCode())
                .adminEmail(admin.getAdminEmail())
                .adminPassword(admin.getAdminPassword())
                .adminDepartment(admin.getAdminDepartment())
                .adminPosition(admin.getAdminPosition())
                .adminName(admin.getAdminName())
                .adminPhone(admin.getAdminPhone())
                .adminAddress(admin.getAdminAddress())
                .adminBirthday(admin.getAdminBirthday())
                .adminJobType(admin.getAdminJobType())
                .adminLevel(admin.getAdminLevel())
                .adminStatus(admin.getAdminStatus())
                .adminLastLoginDate(admin.getAdminLastLoginDate())
                .createdAt(admin.getCreatedAt())
                .updatedAt(admin.getUpdatedAt())
                .build();
    }

    // AdminDTO -> Admin 엔티티 변환
    public Admin toEntity(AdminDTO adminDTO) {

        return Admin.builder()
                .adminCode(adminDTO.getAdminCode())
                .adminEmail(adminDTO.getAdminEmail())
                .adminPassword(adminDTO.getAdminPassword())
                .adminDepartment(adminDTO.getAdminDepartment())
                .adminPosition(adminDTO.getAdminPosition())
                .adminName(adminDTO.getAdminName())
                .adminPhone(adminDTO.getAdminPhone())
                .adminAddress(adminDTO.getAdminAddress())
                .adminBirthday(adminDTO.getAdminBirthday())
                .adminJobType(adminDTO.getAdminJobType())
                .adminLevel(adminDTO.getAdminLevel())
                .adminStatus(adminDTO.getAdminStatus())
                .adminLastLoginDate(adminDTO.getAdminLastLoginDate())
                .createdAt(adminDTO.getCreatedAt())
                .updatedAt(adminDTO.getUpdatedAt())
                .build();
    }
}
