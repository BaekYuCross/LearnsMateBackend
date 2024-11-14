package intbyte4.learnsmate.blacklist.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BlacklistFilterRequestDTO {
    private Long memberCode;
    private String memberName;
    private String memberEmail;
}
