package intbyte4.learnsmate.admin.mapper;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.domain.vo.request.RequestEditAdminVO;
import intbyte4.learnsmate.admin.domain.vo.response.ResponseEditAdminVO;
import intbyte4.learnsmate.admin.domain.vo.response.ResponseFindAdminVO;
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

    // RequestEditAdminVO -> AdminDTO 변환
    public AdminDTO fromVoToDto(RequestEditAdminVO requestEditAdminVO) {
        return AdminDTO.builder()
                .adminEmail(requestEditAdminVO.getAdminEmail())
                .adminPassword(requestEditAdminVO.getAdminPassword())
                .adminName(requestEditAdminVO.getAdminName())
                .adminPhone(requestEditAdminVO.getAdminPhone())
                .adminAddress(requestEditAdminVO.getAdminAddress())
                .adminBirthday(requestEditAdminVO.getAdminBirthday())
                .build();
    }

    // AdminDTO -> FindResponseVO 변환
    public ResponseFindAdminVO fromDtoToFindResponseVO(AdminDTO adminDTO) {
        return ResponseFindAdminVO.builder()
                .adminCode(adminDTO.getAdminCode())
                .adminEmail(adminDTO.getAdminEmail())
                .adminName(adminDTO.getAdminName())
                .adminPhone(adminDTO.getAdminPhone())
                .adminStatus(adminDTO.getAdminStatus())
                .adminLastLoginDate(adminDTO.getAdminLastLoginDate())
                .build();
    }

    // AdminDTO -> EditResponseVO 변환
    public ResponseEditAdminVO fromDtoToEditResponseVO(AdminDTO adminDTO) {
        return ResponseEditAdminVO.builder()
                .adminCode(adminDTO.getAdminCode())
                .adminEmail(adminDTO.getAdminEmail())
                .adminName(adminDTO.getAdminName())
                .adminPhone(adminDTO.getAdminPhone())
                .adminStatus(adminDTO.getAdminStatus())
                .adminLastLoginDate(adminDTO.getAdminLastLoginDate())
                .build();
    }

}
