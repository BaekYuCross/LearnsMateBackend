package intbyte4.learnsmate.contractprocess.service;

import intbyte4.learnsmate.contractprocess.domain.dto.ContractProcessDTO;

import java.util.List;

public interface ContractProcessService {
    // 단건 조회
    ContractProcessDTO getContractProcess(Long contractProcessCode);

    // 강의별 승인과정 절차 조회
    ContractProcessDTO getApprovalProcessByLectureCode(String lectureCode);

    // 계약과정 등록
    ContractProcessDTO createContractProcess(String lectureCode, ContractProcessDTO contractProcessDTO);

    List<ContractProcessDTO> findAll();
}
