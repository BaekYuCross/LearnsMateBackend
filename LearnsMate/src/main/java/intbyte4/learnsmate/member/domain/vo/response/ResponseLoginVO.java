package intbyte4.learnsmate.member.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResponseLoginVO {
    @JsonProperty("member_code")
    private Long memberCode;

    @JsonProperty("member_name")
    private String memberName;

    @JsonProperty("member_email")
    private String memberEmail;

    @JsonProperty("login_history_code")
    private Long loginHistoryCode;
}
