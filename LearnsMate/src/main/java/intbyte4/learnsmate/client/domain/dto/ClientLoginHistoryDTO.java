package intbyte4.learnsmate.client.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ClientLoginHistoryDTO {

    private Long memberCode;
    private String memberName;
    private String memberEmail;
    private Long loginHistoryCode;
}
