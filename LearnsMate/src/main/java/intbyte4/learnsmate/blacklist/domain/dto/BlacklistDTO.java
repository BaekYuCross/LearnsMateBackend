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
    private Long memberCode;
    private Long adminCode;
}
