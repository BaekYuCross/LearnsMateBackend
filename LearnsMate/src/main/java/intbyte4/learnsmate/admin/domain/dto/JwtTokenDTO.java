package intbyte4.learnsmate.admin.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenDTO {
    private String userCode;
    private String userEmail;
    private String userName;
}
