package intbyte4.learnsmate.voc_answer.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.voc.domain.VOC;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import intbyte4.learnsmate.voc.mapper.VOCMapper;
import intbyte4.learnsmate.voc.service.VOCService;
import intbyte4.learnsmate.voc_answer.domain.VOCAnswer;
import intbyte4.learnsmate.voc_answer.domain.dto.VOCAnswerDTO;
import intbyte4.learnsmate.voc_answer.mapper.VOCAnswerMapper;
import intbyte4.learnsmate.voc_answer.repository.VOCAnswerRepository;
import intbyte4.learnsmate.voc_category.domain.VocCategory;
import intbyte4.learnsmate.voc_category.service.VocCategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service("vocAnswerService")
@RequiredArgsConstructor
public class VOCAnswerServiceImpl implements VOCAnswerService {

    private final VOCAnswerRepository vocAnswerRepository;
    private final VOCAnswerMapper vocAnswerMapper;
    private final AdminService adminService;
    private final VOCService vocService;
    private final VOCMapper vocMapper;
    private final VocCategoryService vocCategoryService;
    private final MemberService memberService;

    @Override
    @Transactional
    public VOCAnswerDTO registerVOCAnswer(VOCAnswerDTO vocAnswerDTO) {
        log.info("VOC 답변 등록 중: {}", vocAnswerDTO);
        Admin user = validAdmin(adminService, vocAnswerDTO.getAdminCode(), log);
        VOC voc = getVOC(vocAnswerDTO);

        VOCAnswer vocAnswer = vocAnswerMapper.toEntity(vocAnswerDTO, user, voc);
        vocAnswer.setCreatedAt(LocalDateTime.now());
        vocAnswer.setUpdatedAt(LocalDateTime.now());

        log.info("데이터베이스에 VOC 답변 저장 중: {}", vocAnswer);
        VOCAnswer savedVOCAnswer = vocAnswerRepository.save(vocAnswer);
        log.info("저장된 VOC 답변 객체: {}", savedVOCAnswer);

        return vocAnswerMapper.fromEntityToDTO(savedVOCAnswer);
    }

    @Override
    @Transactional
    public VOCAnswerDTO editVOCAnswer(VOCAnswerDTO vocAnswerDTO) {
        log.info("VOC 답변 수정 중: {}", vocAnswerDTO);

        VOCAnswer vocAnswer = vocAnswerRepository.findById(vocAnswerDTO.getVocAnswerCode()).orElseThrow(() -> new CommonException(StatusEnum.TEMPLATE_NOT_FOUND));
        vocAnswer.setVocAnswerContent(vocAnswerDTO.getVocAnswerContent());
        vocAnswer.setUpdatedAt(LocalDateTime.now());

        log.info("데이터베이스에 수정된 VOC 답변 저장 중: {}", vocAnswer);
        VOCAnswer updatedCampaignTemplate = vocAnswerRepository.save(vocAnswer);
        log.info("수정된 VOC 답변 객체: {}", updatedCampaignTemplate);

        return vocAnswerMapper.fromEntityToDTO(updatedCampaignTemplate);
    }

    @Override
    public VOCAnswerDTO findById(Long vocAnswerCode) {
        VOCAnswer vocAnswer = vocAnswerRepository.findById(vocAnswerCode).orElseThrow(() -> new CommonException(StatusEnum.TEMPLATE_NOT_FOUND));
        return vocAnswerMapper.fromEntityToDTO(vocAnswer);
    }

    private VOC getVOC(VOCAnswerDTO vocAnswerDTO) {
        VOCDTO vocDTO = vocService.findByVOCCode(vocAnswerDTO.getVocCode());
        if (vocDTO == null) {
            log.warn("존재하지 않는 VOC : {}", vocAnswerDTO.getVocCode());
            throw new CommonException(StatusEnum.VOC_NOT_FOUND);
        }
        log.info(vocDTO.toString());

        VocCategory vocCategory = vocCategoryService.findByVocCategoryCode(vocDTO.getVocCategoryCode());
        Member member = memberService.findByStudentCode(vocDTO.getMemberCode());
        return vocMapper.toEntity(vocDTO, vocCategory, member);
    }

    public Admin validAdmin(AdminService adminService, Long adminCode, Logger log) {
        AdminDTO adminDTO = adminService.findByAdminCode(adminCode);
        if (adminDTO == null) {
            log.warn("존재하지 않는 직원 : {}", adminCode);
            throw new CommonException(StatusEnum.ADMIN_NOT_FOUND);
        }
        log.info(adminDTO.toString());

        return adminDTO.convertToEntity();
    }
}
