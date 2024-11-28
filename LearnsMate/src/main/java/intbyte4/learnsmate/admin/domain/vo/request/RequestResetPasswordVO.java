package intbyte4.learnsmate.admin.domain.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestResetPasswordVO {

    @NotBlank
    @Email
    String userEmail;

    @NotBlank
    String userPassword;
}
