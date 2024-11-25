package intbyte4.learnsmate.blacklist.domain.vo.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RequestSaveBlacklistVO {
    private String blackReason;
}
