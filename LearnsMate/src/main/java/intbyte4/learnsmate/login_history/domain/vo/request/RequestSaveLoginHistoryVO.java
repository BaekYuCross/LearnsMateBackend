package intbyte4.learnsmate.login_history.domain.vo.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestSaveLoginHistoryVO {

//    private Long loginHistoryCode;
    private LocalDateTime lastLoginDate;
//    private LocalDateTime lastLogoutDate;
    private Long memberCode;
}
