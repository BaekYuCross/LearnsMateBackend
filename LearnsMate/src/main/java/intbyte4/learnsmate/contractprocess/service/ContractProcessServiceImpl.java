package intbyte4.learnsmate.contractprocess.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.contractprocess.domain.dto.ContractProcessDTO;
import intbyte4.learnsmate.contractprocess.domain.entity.ContractProcess;
import intbyte4.learnsmate.contractprocess.mapper.ContractProcessMapper;
import intbyte4.learnsmate.contractprocess.repository.ContractProcessRepository;
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
public class ContractProcessServiceImpl implements ContractProcessService {
    private final ContractProcessRepository contractProcessRepository;
    private final ContractProcessMapper contractProcessMapper;
    private final AdminService adminService;
    private final AdminMapper adminMapper;
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final LectureService lectureService;
    private final LectureMapper lectureMapper;

    @Override
    public ContractProcessDTO getContractProcess(Long contractProcessCode) {
        ContractProcess contractProcess = contractProcessRepository.findById(contractProcessCode)
                .orElseThrow(() -> new CommonException(StatusEnum.CONTRACT_PROCESS_NOT_FOUND));
        return contractProcessMapper.toDTO(contractProcess);
    }

    @Override
    public ContractProcessDTO getApprovalProcessByLectureCode(String lectureCode) {
        LectureDTO lecturedto = lectureService.getLectureById(lectureCode);

        MemberDTO tutorDTO = memberService.findMemberByMemberCode(lecturedto.getTutorCode(), MemberType.TUTOR);
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);

        Lecture lecture = lectureMapper.toEntity(lecturedto, tutor);

        ContractProcess contractProcess = contractProcessRepository.findByLecture(lecture);

        if (contractProcess == null) {
            throw new CommonException(StatusEnum.CONTRACT_PROCESS_NOT_FOUND);
        }

        return contractProcessMapper.toDTO(contractProcess);
    }

    @Transactional
    @Override
    public ContractProcessDTO createContractProcess(String lectureCode, ContractProcessDTO contractProcessDTO) {
        LectureDTO lectureDTO = lectureService.getLectureById(lectureCode);
        MemberDTO tutorDTO = memberService.findMemberByMemberCode(lectureDTO.getTutorCode(), MemberType.TUTOR);
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);

        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor);

        AdminDTO adminDTO = adminService.findByAdminCode(contractProcessDTO.getAdminCode());
        Admin admin = adminMapper.toEntity(adminDTO);

        ContractProcess existingContractProcess = contractProcessRepository
                .findByLectureAndApprovalProcess(lecture, contractProcessDTO.getApprovalProcess())
                .orElse(null);

        if (existingContractProcess != null) {
            throw new CommonException(StatusEnum.EXISTING_CONTRACT_PROCESS);
        }

        ContractProcess contractProcess = ContractProcess.builder()
                .lecture(lecture)
                .admin(admin)
                .approvalProcess(contractProcessDTO.getApprovalProcess())
                .createdAt(LocalDateTime.now())
                .note(null)
                .build();

        contractProcessRepository.save(contractProcess);

        int contractProcessCount = contractProcessRepository.countByLecture(lecture);
        LectureDTO lecturedto = lectureMapper.toDTO(lecture);

        if (contractProcessCount == 7) {
            lectureService.updateLectureConfirmStatus(lecturedto.getLectureCode());
        }

        return contractProcessMapper.toDTO(contractProcess);
    }

    @Override
    public List<ContractProcessDTO> findAll() {
        List<ContractProcess> contractProcessList = contractProcessRepository.findAll();
        List<ContractProcessDTO> contractProcessDTOList = new ArrayList<>();
        for (ContractProcess contractProcess : contractProcessList) {
            contractProcessDTOList.add(contractProcessMapper.toDTO(contractProcess));
        }
        return contractProcessDTOList;
    }
}
