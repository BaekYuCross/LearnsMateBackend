package intbyte4.learnsmate.contractprocess.service;

import intbyte4.learnsmate.contractprocess.domain.dto.ContractProcessDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ContractProcessService {
    // 단건 조회
    ContractProcessDTO getContractProcess(Long contractProcessCode);

    // 강사별 강의별 승인과정 절차 조회
    List<ContractProcessDTO> getApprovalProcessByLectureCode(Long tutorCode);

}
