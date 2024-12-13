package intbyte4.learnsmate.client.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ClientLoginDTO {

    private String memberEmail;
    private String memberPassword;
}
