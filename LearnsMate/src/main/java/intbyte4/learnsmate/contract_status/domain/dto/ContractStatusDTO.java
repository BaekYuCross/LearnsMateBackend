package intbyte4.learnsmate.contract_status.domain.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ContractStatusDTO {
    private Long contractStatusCode;
    private Integer approvalStatus;
    private LocalDateTime createdAt;
    private String note;
    private String lectureCode;
    private Long adminCode;
}
