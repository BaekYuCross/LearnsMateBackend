package intbyte4.learnsmate.contractprocess.domain.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ContractProcessDTO {

    private Long contractProcessCode;
    private Integer approvalProcess;
    private LocalDateTime createdAt;
    private String note;
    private String lectureCode;
    private Long adminCode;
}
