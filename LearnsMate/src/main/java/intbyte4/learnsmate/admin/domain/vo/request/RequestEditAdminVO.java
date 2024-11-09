package intbyte4.learnsmate.admin.domain.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RequestEditAdminVO {

    @JsonProperty("admin_email")
    private String adminEmail;

    @JsonProperty("admin_password")
    private String adminPassword;

    @JsonProperty("admin_name")
    private String adminName;

    @JsonProperty("admin_phone")
    private String adminPhone;

    @JsonProperty("admin_address")
    private String adminAddress;

    @JsonProperty("admin_birthday")
    private LocalDateTime adminBirthday;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
