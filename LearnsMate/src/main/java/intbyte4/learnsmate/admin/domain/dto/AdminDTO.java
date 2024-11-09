package intbyte4.learnsmate.admin.domain.dto;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDTO {
    private Long adminCode;
    private String adminEmail;
    private String adminPassword;
    private String adminDepartment;
    private String adminPosition;
    private String adminName;
    private String adminPhone;
    private String adminAddress;
    private LocalDateTime adminBirthday;
    private String adminJobType;
    private String adminLevel;
    private Boolean adminStatus;
    private LocalDateTime adminLastLoginDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
