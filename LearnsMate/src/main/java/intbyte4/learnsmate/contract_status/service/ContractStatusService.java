package intbyte4.learnsmate.contract_status.service;

import intbyte4.learnsmate.contract_status.domain.dto.ContractStatusDTO;

import java.util.List;

public interface ContractStatusService {
    // 단건 조회
    ContractStatusDTO getContractProcess(Long contractProcessCode);

    // 강의별 승인과정 절차 조회
    List<ContractStatusDTO> getApprovalProcessByLectureCode(String lectureCode);

    // 계약과정 등록
    ContractStatusDTO createContractProcess(String lectureCode, ContractStatusDTO contractStatusDTO);

    List<ContractStatusDTO> findAll();
}
