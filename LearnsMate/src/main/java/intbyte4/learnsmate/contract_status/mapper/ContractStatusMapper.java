package intbyte4.learnsmate.contract_status.mapper;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.contract_status.domain.dto.ContractStatusDTO;
import intbyte4.learnsmate.contract_status.domain.entity.ContractStatus;
import intbyte4.learnsmate.contract_status.domain.vo.request.ResponseRegisterContractStatusVO;
import intbyte4.learnsmate.contract_status.domain.vo.response.RequestRegisterContractStatusVO;
import intbyte4.learnsmate.contract_status.domain.vo.response.ResponseFindContractStatusVO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import org.springframework.stereotype.Component;

@Component
public class ContractStatusMapper {

    // ContractProcess 엔티티를 ContractProcessDTO로 변환
    public ContractStatusDTO toDTO(ContractStatus entity) {
        return ContractStatusDTO.builder()
                .contractStatusCode(entity.getContractStatusCode())
                .approvalStatus(entity.getApprovalStatus())
                .createdAt(entity.getCreatedAt())
                .note(entity.getNote())
                .lectureCode(entity.getLecture().getLectureCode())
                .adminCode(entity.getAdmin().getAdminCode())
                .build();
    }

    // ContractProcessDTO를 ContractProcess 엔티티로 변환
    public ContractStatus toEntity(ContractStatusDTO dto, Lecture lecture, Admin admin) {
        return ContractStatus.builder()
                .contractStatusCode(dto.getContractStatusCode())
                .approvalStatus(dto.getApprovalStatus())
                .createdAt(dto.getCreatedAt())
                .note(dto.getNote())
                .lecture(lecture)
                .admin(admin)
                .build();
    }

    // ContractProcessDTO를 ResponseContractProcessVO로 변환
    public ResponseFindContractStatusVO fromDtoToResponseVO(ContractStatusDTO dto) {
        return ResponseFindContractStatusVO.builder()
                .contractStatusCode(dto.getContractStatusCode())
                .approvalStatus(dto.getApprovalStatus())
                .createdAt(dto.getCreatedAt())
                .note(dto.getNote())
                .lectureCode(dto.getLectureCode())
                .adminCode(dto.getAdminCode())
                .build();
    }

    public ResponseRegisterContractStatusVO fromDtoToRegisterResponseVO(ContractStatusDTO dto) {
        return ResponseRegisterContractStatusVO.builder()
                .contractStatusCode(dto.getContractStatusCode())
                .approvalStatus(dto.getApprovalStatus())
                .createdAt(dto.getCreatedAt())
                .note(dto.getNote())
                .lectureCode(dto.getLectureCode())
                .adminCode(dto.getAdminCode())
                .build();
    }

    public ContractStatusDTO fromRegisterRequestVOtoDto(RequestRegisterContractStatusVO vo) {
        return ContractStatusDTO.builder()
                .contractStatusCode(vo.getContractStatusCode())
                .approvalStatus(vo.getApprovalStatus())
                .createdAt(vo.getCreatedAt())
                .note(vo.getNote())
                .lectureCode(vo.getLectureCode())
                .adminCode(vo.getAdminCode())
                .build();
    }



}
