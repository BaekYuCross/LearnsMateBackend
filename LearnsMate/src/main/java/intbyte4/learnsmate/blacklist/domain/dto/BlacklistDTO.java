package intbyte4.learnsmate.blacklist.domain.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BlacklistDTO {

    private Long blackCode;
    private String blackReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long memberCode;
    private Long reportCode;
    private Long adminCode;
}
