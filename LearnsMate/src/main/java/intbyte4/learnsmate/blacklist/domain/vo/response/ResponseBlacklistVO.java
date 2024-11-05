package intbyte4.learnsmate.blacklist.domain.vo.response;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResponseBlacklistVO {

    private Long blackCode;
    private String blackReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long memberCode;
    private Long reportCode;
    private Long adminCode;
}
