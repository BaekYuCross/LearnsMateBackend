package intbyte4.learnsmate.contractprocess.service;

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
import intbyte4.learnsmate.lecture_category.domain.dto.LectureCategoryDTO;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategory;
import intbyte4.learnsmate.lecture_category.mapper.LectureCategoryMapper;
import intbyte4.learnsmate.lecture_category.service.LectureCategoryService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ContractProcessServiceImpl implements ContractProcessService {
    private final ContractProcessRepository contractProcessRepository;
    private final ContractProcessMapper contractProcessMapper;
    private final LectureCategoryService lectureCategoryService;
    private final LectureCategoryMapper lectureCategoryMapper;
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final LectureService lectureService;
    private final LectureMapper lectureMapper;


    // 단건 조회
    @Override
    public ContractProcessDTO getContractProcess(Long contractProcessCode) {
        ContractProcess contractProcess = contractProcessRepository.findById(contractProcessCode)
                .orElseThrow(() -> new CommonException(StatusEnum.CONTRACT_PROCESS_NOT_FOUND));
        return contractProcessMapper.toDTO(contractProcess);
    }


    // 강의별 승인과정 절차 조회
    @Override
    public ContractProcessDTO getApprovalProcessByLectureCode(Long lectureCode) {
        // 강의 정보 조회
        LectureDTO lecturedto = lectureService.getLectureById(lectureCode);

        // 강사 정보 조회
        MemberDTO tutorDTO = memberService.findMemberByMemberCode(lecturedto.getLectureCode(), MemberType.TUTOR);
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);

        // 강의 카테고리 정보 조회
        LectureCategoryDTO lectureCategoryDTO = lectureCategoryService.findByLectureCategoryCode(lecturedto.getLectureCategoryCode());
        LectureCategory lectureCategory = lectureCategoryMapper.toEntity(lectureCategoryDTO);

        // Lecture 객체 변환
        Lecture lecture = lectureMapper.toEntity(lecturedto, tutor, lectureCategory);

        // 강의에 대한 계약 과정 조회
        ContractProcess contractProcess = contractProcessRepository.findByLecture(lecture);

        if (contractProcess == null) {
            throw new CommonException(StatusEnum.CONTRACT_PROCESS_NOT_FOUND);
        }

        // ContractProcess를 DTO로 변환
        return contractProcessMapper.toDTO(contractProcess);
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
