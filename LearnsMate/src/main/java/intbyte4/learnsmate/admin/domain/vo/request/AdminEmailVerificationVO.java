package intbyte4.learnsmate.admin.domain.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminEmailVerificationVO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private Long adminCode;
}
