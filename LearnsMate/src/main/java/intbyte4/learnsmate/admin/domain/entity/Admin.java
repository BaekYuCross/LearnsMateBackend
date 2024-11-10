package intbyte4.learnsmate.admin.domain.entity;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "admins")
@Table(name = "admins")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_code", nullable = false)
    private Long adminCode;

    @Column(name = "admin_email", nullable = false)
    private String adminEmail;

    @Column(name = "admin_password", nullable = false)
    private String adminPassword;

    @Column(name = "admin_department", nullable = false)
    private String adminDepartment;

    @Column(name = "admin_position", nullable = false)
    private String adminPosition;

    @Column(name = "admin_name", nullable = false)
    private String adminName;

    @Column(name = "admin_phone", nullable = false)
    private String adminPhone;

    @Column(name = "admin_address", nullable = false)
    private String adminAddress;

    @Column(name = "admin_birthday", nullable = false)
    private LocalDateTime adminBirthday;

    @Column(name = "admin_job_type", nullable = false)
    private String adminJobType;

    @Column(name = "admin_level", nullable = false)
    private String adminLevel;

    @Column(name = "admin_status", nullable = false)
    private Boolean adminStatus;

    @Column(name = "admin_last_login_date", nullable = false)
    private LocalDateTime adminLastLoginDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void toUpdate(AdminDTO request) {
        this.adminEmail = request.getAdminEmail();
        this.adminPassword = request.getAdminPassword();
        this.adminName = request.getAdminName();
        this.adminPhone = request.getAdminPhone();
        this.adminAddress = request.getAdminAddress();
        this.adminBirthday = request.getAdminBirthday();
        this.updatedAt = LocalDateTime.now();
    }
}
