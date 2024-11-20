package intbyte4.learnsmate.contract_status.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.contract_status.domain.dto.ContractStatusDTO;
import intbyte4.learnsmate.contract_status.domain.entity.ContractStatus;
import intbyte4.learnsmate.contract_status.mapper.ContractStatusMapper;
import intbyte4.learnsmate.contract_status.repository.ContractStatusRepository;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractStatusServiceImpl implements ContractStatusService {
    private final ContractStatusRepository contractStatusRepository;
    private final ContractStatusMapper contractStatusMapper;
    private final AdminService adminService;
    private final AdminMapper adminMapper;
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final LectureService lectureService;
    private final LectureMapper lectureMapper;

    @Override
    public ContractStatusDTO getContractProcess(Long contractProcessCode) {
        ContractStatus contractStatus = contractStatusRepository.findById(contractProcessCode)
                .orElseThrow(() -> new CommonException(StatusEnum.CONTRACT_PROCESS_NOT_FOUND));
        return contractStatusMapper.toDTO(contractStatus);
    }

    @Override
    public ContractStatusDTO getApprovalProcessByLectureCode(String lectureCode) {
        LectureDTO lecturedto = lectureService.getLectureById(lectureCode);

        MemberDTO tutorDTO = memberService.findMemberByMemberCode(lecturedto.getTutorCode(), MemberType.TUTOR);
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);

        Lecture lecture = lectureMapper.toEntity(lecturedto, tutor);

        ContractStatus contractStatus = contractStatusRepository.findByLecture(lecture);

        if (contractStatus == null) {
            throw new CommonException(StatusEnum.CONTRACT_PROCESS_NOT_FOUND);
        }

        return contractStatusMapper.toDTO(contractStatus);
    }

    @Transactional
    @Override
    public ContractStatusDTO createContractProcess(String lectureCode, ContractStatusDTO contractStatusDTO) {
        LectureDTO lectureDTO = lectureService.getLectureById(lectureCode);
        MemberDTO tutorDTO = memberService.findMemberByMemberCode(lectureDTO.getTutorCode(), MemberType.TUTOR);
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);

        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor);

        AdminDTO adminDTO = adminService.findByAdminCode(contractStatusDTO.getAdminCode());
        Admin admin = adminMapper.toEntity(adminDTO);

        ContractStatus existingContractStatus = contractStatusRepository
                .findByLectureAndApprovalStatus(lecture, contractStatusDTO.getApprovalStatus())
                .orElse(null);

        if (existingContractStatus != null) {
            throw new CommonException(StatusEnum.EXISTING_CONTRACT_PROCESS);
        }

        ContractStatus contractStatus = ContractStatus.builder()
                .lecture(lecture)
                .admin(admin)
                .approvalStatus(contractStatusDTO.getApprovalStatus())
                .createdAt(LocalDateTime.now())
                .note(null)
                .build();

        contractStatusRepository.save(contractStatus);

        int contractProcessCount = contractStatusRepository.countByLecture(lecture);
        LectureDTO lecturedto = lectureMapper.toDTO(lecture);

        if (contractProcessCount == 7) {
            lectureService.updateLectureConfirmStatus(lecturedto.getLectureCode());
        }

        return contractStatusMapper.toDTO(contractStatus);
    }

    @Override
    public List<ContractStatusDTO> findAll() {
        List<ContractStatus> contractStatusList = contractStatusRepository.findAll();
        List<ContractStatusDTO> contractStatusDTOList = new ArrayList<>();
        for (ContractStatus contractStatus : contractStatusList) {
            contractStatusDTOList.add(contractStatusMapper.toDTO(contractStatus));
        }
        return contractStatusDTOList;
    }
}
