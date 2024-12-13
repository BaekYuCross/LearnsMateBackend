package intbyte4.learnsmate.member.domain.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RequestLogoutVO {

    @JsonProperty("login_history_code")
    private Long loginHistoryCode;
}
