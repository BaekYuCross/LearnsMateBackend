package intbyte4.learnsmate.voc_answer.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.voc.domain.Voc;
import intbyte4.learnsmate.voc.domain.dto.VocDTO;
import intbyte4.learnsmate.voc.mapper.VocMapper;
import intbyte4.learnsmate.voc.service.VocService;
import intbyte4.learnsmate.voc_answer.domain.VocAnswer;
import intbyte4.learnsmate.voc_answer.domain.dto.VocAnswerDTO;
import intbyte4.learnsmate.voc_answer.mapper.VocAnswerMapper;
import intbyte4.learnsmate.voc_answer.repository.VocAnswerRepository;
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
public class VocAnswerServiceImpl implements VocAnswerService {

    private final VocAnswerRepository vocAnswerRepository;
    private final VocAnswerMapper vocAnswerMapper;
    private final AdminService adminService;
    private final VocService vocService;
    private final VocMapper vocMapper;
    private final VocCategoryService vocCategoryService;
    private final MemberService memberService;

    @Override
    @Transactional
    public VocAnswerDTO registerVocAnswer(VocAnswerDTO vocAnswerDTO) {
        log.info("VOC 답변 등록 중: {}", vocAnswerDTO);
        Admin user = validAdmin(adminService, vocAnswerDTO.getAdminCode(), log, vocAnswerDTO);
        Voc voc = getVoc(vocAnswerDTO);

        VocAnswer vocAnswer = vocAnswerMapper.toEntity(vocAnswerDTO, user, voc);
        vocAnswer.setCreatedAt(LocalDateTime.now());
        vocAnswer.setUpdatedAt(LocalDateTime.now());

        log.info("데이터베이스에 VOC 답변 저장 중: {}", vocAnswer);
        VocAnswer savedVocAnswer = vocAnswerRepository.save(vocAnswer);
        log.info("저장된 VOC 답변 객체: {}", savedVocAnswer);

        return vocAnswerMapper.fromEntityToDto(savedVocAnswer);
    }

    private Voc getVoc(VocAnswerDTO vocAnswerDTO) {
        VocDTO vocDTO = vocService.findByVocCode(vocAnswerDTO.getVocCode());
        if (vocDTO == null) {
            log.warn("존재하지 않는 VOC : {}", vocAnswerDTO.getVocCode());
            throw new CommonException(StatusEnum.VOC_NOT_FOUND);
        }
        log.info(vocDTO.toString());

        VocCategory vocCategory = vocCategoryService.findByVocCategoryCode(vocDTO.getVocCategoryCode());
        Member member = memberService.findByStudentCode(vocDTO.getMemberCode());
        return vocMapper.toEntity(vocDTO, vocCategory, member);
    }

    public Admin validAdmin(AdminService adminService, Long adminCode, Logger log, VocAnswerDTO vocAnswerDTO) {
        AdminDTO adminDTO = adminService.findByAdminCode(adminCode);
        if (adminDTO == null) {
            log.warn("존재하지 않는 직원 : {}", adminCode);
            throw new CommonException(StatusEnum.ADMIN_NOT_FOUND);
        }
        log.info(adminDTO.toString());

        return adminDTO.convertToEntity();
    }
}
