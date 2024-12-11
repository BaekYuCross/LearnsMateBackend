package intbyte4.learnsmate.voc_answer.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.voc.domain.VOC;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import intbyte4.learnsmate.voc.mapper.VOCMapper;
import intbyte4.learnsmate.voc.service.VOCService;
import intbyte4.learnsmate.voc_answer.domain.VOCAnswer;
import intbyte4.learnsmate.voc_answer.domain.dto.VOCAnswerDTO;
import intbyte4.learnsmate.voc_answer.mapper.VOCAnswerMapper;
import intbyte4.learnsmate.voc_answer.repository.VOCAnswerRepository;
import intbyte4.learnsmate.voc_category.domain.dto.VOCCategoryDTO;
import intbyte4.learnsmate.voc_category.mapper.VOCCategoryMapper;
import intbyte4.learnsmate.voc_category.service.VOCCategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
@Service("vocAnswerService")
@RequiredArgsConstructor
public class VOCAnswerServiceImpl implements VOCAnswerService {

    private final VOCAnswerRepository vocAnswerRepository;
    private final VOCAnswerMapper vocAnswerMapper;
    private final AdminService adminService;
    private final VOCService vocService;
    private final VOCMapper vocMapper;
    private final VOCCategoryService vocCategoryService;
    private final MemberService memberService;
    private final AdminMapper adminMapper;
    private final VOCCategoryMapper vocCategoryMapper;
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public VOCAnswerDTO registerVOCAnswer(VOCAnswerDTO vocAnswerDTO) {
        log.info("VOC 답변 등록 중: {}", vocAnswerDTO);
        Admin user = validAdmin(adminService, vocAnswerDTO.getAdminCode(), log);
        VOC voc = getVOC(vocAnswerDTO);

        VOCAnswer vocAnswer = vocAnswerMapper.toEntity(vocAnswerDTO, user, voc);
        vocAnswer.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        vocAnswer.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        log.info("데이터베이스에 VOC 답변 저장 중: {}", vocAnswer);
        VOCAnswer savedVOCAnswer = vocAnswerRepository.save(vocAnswer);
        log.info("저장된 VOC 답변 객체: {}", savedVOCAnswer);

        vocService.updateVOCAnswerStatus(voc.getVocCode(), true);

        return vocAnswerMapper.fromEntityToDTO(savedVOCAnswer);
    }

    @Override
    @Transactional
    public VOCAnswerDTO editVOCAnswer(VOCAnswerDTO vocAnswerDTO) {
        log.info("VOC 답변 수정 중: {}", vocAnswerDTO);

        VOCAnswer vocAnswer = vocAnswerRepository.findById(vocAnswerDTO.getVocAnswerCode()).orElseThrow(() -> new CommonException(StatusEnum.VOC_ANSWER_NOT_FOUND));
        vocAnswer.setVocAnswerContent(vocAnswerDTO.getVocAnswerContent());
        vocAnswer.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        log.info("데이터베이스에 수정된 VOC 답변 저장 중: {}", vocAnswer);
        VOCAnswer updatedCampaignTemplate = vocAnswerRepository.save(vocAnswer);
        log.info("수정된 VOC 답변 객체: {}", updatedCampaignTemplate);

        return vocAnswerMapper.fromEntityToDTO(updatedCampaignTemplate);
    }

    @Override
    public VOCAnswerDTO findById(Long vocAnswerCode) {
        VOCAnswer vocAnswer = vocAnswerRepository.findById(vocAnswerCode).orElseThrow(() -> new CommonException(StatusEnum.VOC_ANSWER_NOT_FOUND));
        return vocAnswerMapper.fromEntityToDTO(vocAnswer);
    }

    @Override
    public VOCAnswerDTO findByVOCCode(String vocCode) {
        Optional<VOCAnswer> optionalVocAnswer = vocAnswerRepository.findByVoc_VocCode(vocCode);

        return optionalVocAnswer
                .map(vocAnswerMapper::fromEntityToDTO)
                .orElse(null);
    }

    private VOC getVOC(VOCAnswerDTO vocAnswerDTO) {
        VOCDTO vocDTO = vocService.findByVOCCode(vocAnswerDTO.getVocCode());
        if (vocDTO == null) {
            log.warn("존재하지 않는 VOC : {}", vocAnswerDTO.getVocCode());
            throw new CommonException(StatusEnum.VOC_NOT_FOUND);
        }
        log.info(vocDTO.toString());

        VOCCategoryDTO vocCategoryDTO = vocCategoryService.findByVocCategoryCode(vocDTO.getVocCategoryCode());
        MemberDTO memberDTO = memberService.findById(vocDTO.getMemberCode());
        return vocMapper.toEntity(vocDTO, vocCategoryMapper.toEntity(vocCategoryDTO), memberMapper.fromMemberDTOtoMember(memberDTO));
    }

    public Admin validAdmin(AdminService adminService, Long adminCode, Logger log) {
        log.info("Checking admin with adminCode: {}", adminCode); // adminCode 값 확인
        AdminDTO adminDTO = adminService.findByAdminCode(adminCode);
        if (adminDTO == null) {
            log.warn("Admin not found for code: {}", adminCode);
            throw new CommonException(StatusEnum.ADMIN_NOT_FOUND);
        }
        log.info("Found admin: {}", adminDTO); // 반환된 adminDTO 확인

        Admin admin = adminMapper.toEntity(adminDTO);
        log.info("Mapped admin entity: {}", admin); // Admin Entity 매핑 확인
        return admin;
    }
}
