package intbyte4.learnsmate.admin.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseEditAdminVO {

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
