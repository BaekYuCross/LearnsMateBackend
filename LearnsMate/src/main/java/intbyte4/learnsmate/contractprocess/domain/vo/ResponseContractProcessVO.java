package intbyte4.learnsmate.contractprocess.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ResponseContractProcessVO {

    private Long contractProcessCode;
    private Integer approvalProcess;
    private LocalDateTime createdAt;
    private String note;
    private Long lectureCode;
    private Long adminCode;
}
