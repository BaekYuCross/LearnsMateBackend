package intbyte4.learnsmate.contractprocess.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.common.mapper.MemberMapper;
import intbyte4.learnsmate.contractprocess.domain.dto.ContractProcessDTO;
import intbyte4.learnsmate.contractprocess.domain.entity.ContractProcess;
import intbyte4.learnsmate.contractprocess.mapper.ContractProcessMapper;
import intbyte4.learnsmate.contractprocess.repository.ContractProcessRepository;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractProcessServiceImpl implements ContractProcessService {
    private final ContractProcessRepository contractProcessRepository;
    private final ContractProcessMapper contractProcessMapper;
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final LectureMapper lectureMapper;
    private final LectureService lectureService;
    private final AdminService adminService;


   // 단건 조회
    @Override
    public ContractProcessDTO getContractProcess(Long contractProcessCode) {
        ContractProcess contractProcess = contractProcessRepository.findById(contractProcessCode)
                .orElseThrow(() -> new CommonException(StatusEnum.CONTRACT_PROCESS_NOT_FOUND));
        return contractProcessMapper.toDTO(contractProcess);
    }

    // 강사별 강의별 승인과정 절차 조회
    @Override
    public List<ContractProcessDTO> getApprovalProcessByLectureCode(Long tutorCode) {
        // 해당 강사의 모든 강의를 조회
        List<LectureDTO> lectures = lectureService.getLecturesByTutorCode(tutorCode);

        if (lectures.isEmpty()) {
            throw new CommonException(StatusEnum.LECTURE_NOT_FOUND);
        }

        // 각 강의에 대해 승인 과정 절차를 조회하고, 이를 ContractProcessDTO로 변환하여 반환
        return lectures.stream()
                .map(lecture -> {
                    // 강의의 lectureCode를 통해 해당 강의의 승인 과정 절차를 조회
                    ContractProcess contractProcess = contractProcessRepository.findByLectureCode(lecture.getLectureCode())
                            .orElseThrow(() -> new CommonException(StatusEnum.CONTRACT_PROCESS_NOT_FOUND));

                    // ContractProcess를 ContractProcessDTO로 변환
                    return contractProcessMapper.toDTO(contractProcess);
                })
                .collect(Collectors.toList());
    }

    // 계약과정 등록
//    @Transactional
//    @Override
//    public ContractProcessDTO createContractProcess(Long lectureCode, ContractProcessDTO contractProcessDTO) {
//        LectureDTO lectureDTO = lectureService.getLectureById(lectureCode);
//
//        Member tutor = memberService.findByMemberCode(lectureDTO.getTutorCode());
//
//        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor);
//
//        AdminDTO adminDTO = adminService.findByAdminCode(contractProcessDTO.getAdminCode());
//        Admin admin = adminDTO.convertToEntity();
//
//        ContractProcess contractProcess = ContractProcess.builder()
//                .lecture(lecture)
//                .admin(admin)
//                .approvalProcess(1)
//                .createdAt(LocalDateTime.now())
//                .note("신규 계약 등록")
//                .build();
//
//        contractProcessRepository.save(contractProcess);
//
//        return contractProcessMapper.toDTO(contractProcess);
//    }






}
