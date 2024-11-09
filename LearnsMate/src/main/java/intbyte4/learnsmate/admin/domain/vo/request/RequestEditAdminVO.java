package intbyte4.learnsmate.admin.domain.vo.request;


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
public class RequestEditAdminVO {

    private String adminEmail;
    private String adminPassword;
    private String adminName;
    private String adminPhone;
    private String adminAddress;
    private LocalDateTime adminBirthday;
    private LocalDateTime updatedAt;
}
