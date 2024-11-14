package intbyte4.learnsmate.blacklist.domain.dto;

import intbyte4.learnsmate.member.domain.MemberType;
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
    private MemberType memberType;
}
