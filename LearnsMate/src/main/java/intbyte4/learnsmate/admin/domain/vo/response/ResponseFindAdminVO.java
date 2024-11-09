package intbyte4.learnsmate.admin.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResponseFindAdminVO {

    @JsonProperty("admin_code")
    private Long adminCode;

    @JsonProperty("admin_email")
    private String adminEmail;

    @JsonProperty("admin_password")
    private String adminPassword;

    @JsonProperty("admin_department")
    private String adminDepartment;

    @JsonProperty("admin_position")
    private String adminPosition;

    @JsonProperty("admin_name")
    private String adminName;

    @JsonProperty("admin_phone")
    private String adminPhone;

    @JsonProperty("admin_address")
    private String adminAddress;

    @JsonProperty("admin_birthday")
    private LocalDateTime adminBirthday;

    @JsonProperty("admin_job_type")
    private String adminJobType;

    @JsonProperty("admin_level")
    private String adminLevel;

    @JsonProperty("admin_status")
    private Boolean adminStatus;

    @JsonProperty("admin_last_login_date")
    private LocalDateTime adminLastLoginDate;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
