package intbyte4.learnsmate.blacklist.domain.vo.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestFilterBlacklistMemberVO {
    private Long memberCode;
    private String memberName;
    private String memberEmail;
}
