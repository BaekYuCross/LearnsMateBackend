package intbyte4.learnsmate.contractprocess.mapper;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.contractprocess.domain.dto.ContractProcessDTO;
import intbyte4.learnsmate.contractprocess.domain.entity.ContractProcess;
import intbyte4.learnsmate.contractprocess.domain.vo.ResponseContractProcessVO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ContractProcessMapper {

    // ContractProcess 엔티티를 ContractProcessDTO로 변환
    public ContractProcessDTO toDTO(ContractProcess entity) {
        return ContractProcessDTO.builder()
                .contractProcessCode(entity.getContractProcessCode())
                .approvalProcess(entity.getApprovalProcess())
                .createdAt(entity.getCreatedAt())
                .note(entity.getNote())
                .lectureCode(entity.getLecture().getLectureCode())
                .adminCode(entity.getAdmin().getAdminCode())
                .build();
    }

    // ContractProcessDTO를 ContractProcess 엔티티로 변환
    public ContractProcess toEntity(ContractProcessDTO dto, Lecture lecture, Admin admin) {
        return ContractProcess.builder()
                .contractProcessCode(dto.getContractProcessCode())
                .approvalProcess(dto.getApprovalProcess())
                .createdAt(dto.getCreatedAt())
                .note(dto.getNote())
                .lecture(lecture)
                .admin(admin)
                .build();
    }

    // ContractProcessDTO를 ResponseContractProcessVO로 변환
    public ResponseContractProcessVO fromDtoToResponseVO(ContractProcessDTO dto) {
        return ResponseContractProcessVO.builder()
                .contractProcessCode(dto.getContractProcessCode())
                .approvalProcess(dto.getApprovalProcess())
                .createdAt(dto.getCreatedAt())
                .note(dto.getNote())
                .lectureCode(dto.getLectureCode())
                .adminCode(dto.getAdminCode())
                .build();
    }


}
