package intbyte4.learnsmate.voc.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import intbyte4.learnsmate.voc.domain.vo.response.ResponseFindVOCVO;
import intbyte4.learnsmate.voc.mapper.VOCMapper;
import intbyte4.learnsmate.voc_answer.domain.dto.VOCAnswerDTO;
import intbyte4.learnsmate.voc_answer.service.VOCAnswerService;
import intbyte4.learnsmate.voc_category.domain.dto.VOCCategoryDTO;
import intbyte4.learnsmate.voc_category.service.VOCCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VOCFacade {
    private final VOCService vocService;
    private final MemberService memberService;
    private final AdminService adminService;
    private final VOCAnswerService vocAnswerService;
    private final VOCCategoryService vocCategoryService;
    private final VOCMapper vocMapper;

    public List<ResponseFindVOCVO> findAllVOCs() {
        List<VOCDTO> vocList = vocService.findAllByVOC();
        List<ResponseFindVOCVO> responseList = new ArrayList<>();

        for (VOCDTO vocDTO : vocList) {
            MemberDTO memberDTO = memberService.findById(vocDTO.getMemberCode());
            VOCCategoryDTO categoryDTO = vocCategoryService.findByVocCategoryCode(vocDTO.getVocCategoryCode());

            VOCAnswerDTO vocAnswerDTO = vocAnswerService.findByVOCCode(vocDTO.getVocCode());
            AdminDTO adminDTO = vocAnswerDTO != null ? adminService.findByAdminCode(vocAnswerDTO.getAdminCode()) : null;

            ResponseFindVOCVO responseVO = vocMapper.fromDTOToResponseVO(vocDTO, memberDTO, categoryDTO, adminDTO);

            responseList.add(responseVO);
        }

        return responseList;
    }


    public ResponseFindVOCVO findVOC(String vocCode) {
        VOCDTO vocDTO = vocService.findByVOCCode(vocCode);
        MemberDTO memberDTO = memberService.findById(vocDTO.getMemberCode());
        VOCCategoryDTO categoryDTO = vocCategoryService.findByVocCategoryCode(vocDTO.getVocCategoryCode());
        VOCAnswerDTO vocAnswerDTO = vocAnswerService.findByVOCCode(vocDTO.getVocCode());
        AdminDTO adminDTO = adminService.findByAdminCode(vocAnswerDTO.getAdminCode());

        return vocMapper.fromDTOToResponseVO(vocDTO, memberDTO, categoryDTO, adminDTO);
    }
}