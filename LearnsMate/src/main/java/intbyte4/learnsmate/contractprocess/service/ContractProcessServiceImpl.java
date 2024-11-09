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







}
