package intbyte4.learnsmate.member.domain.vo.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseLoginVO {

    private Long memberCode;
    private String memberName;
    private String memberEmail;
    private Long loginHistoryCode;
}
