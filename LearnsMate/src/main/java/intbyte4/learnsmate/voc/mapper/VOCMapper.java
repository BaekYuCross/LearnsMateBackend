package intbyte4.learnsmate.voc.mapper;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.voc.domain.VOC;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import intbyte4.learnsmate.voc.domain.dto.VOCFilterRequestDTO;
import intbyte4.learnsmate.voc.domain.vo.request.RequestFilterVOCVO;
import intbyte4.learnsmate.voc.domain.vo.response.ResponseFindVOCVO;
import intbyte4.learnsmate.voc_answer.domain.dto.VOCAnswerDTO;
import intbyte4.learnsmate.voc_category.domain.VOCCategory;
import intbyte4.learnsmate.voc_category.domain.dto.VOCCategoryDTO;
import org.springframework.stereotype.Component;

@Component
public class VOCMapper {
    public VOCDTO fromEntityToDTO(VOC voc) {
        return VOCDTO.builder()
                .vocCode(voc.getVocCode())
                .vocContent(voc.getVocContent())
                .createdAt(voc.getCreatedAt())
                .vocAnswerStatus(voc.getVocAnswerStatus())
                .vocAnswerSatisfaction(voc.getVocAnswerSatisfaction())
                .vocCategoryCode(voc.getVocCategory().getVocCategoryCode())
                .memberCode(voc.getMember().getMemberCode())
                .build();
    }

    public ResponseFindVOCVO fromDTOToResponseVO(VOCDTO vocDTO, MemberDTO memberDTO, VOCCategoryDTO categoryDTO, VOCAnswerDTO vocAnswerDTO, AdminDTO adminDTO) {
        return ResponseFindVOCVO.builder()
                .vocCode(vocDTO.getVocCode())
                .vocContent(vocDTO.getVocContent())
                .vocCategoryName(categoryDTO.getVocCategoryName())
                .memberType(String.valueOf(memberDTO.getMemberType()))
                .memberName(memberDTO.getMemberName())
                .memberCode(memberDTO.getMemberCode())
                .adminName(adminDTO != null ? adminDTO.getAdminName() : "-")
                .createdAt(vocDTO.getCreatedAt())
                .vocAnswerStatus(vocDTO.getVocAnswerStatus())
                .vocAnswerSatisfaction(vocDTO.getVocAnswerSatisfaction())
                .vocAnswerCode(vocAnswerDTO.getVocAnswerCode())
                .vocAnswerContent(vocAnswerDTO.getVocAnswerContent())
                .build();
    }


    public VOC toEntity(VOCDTO vocDTO, VOCCategory vocCategory, Member member) {
        return VOC.builder()
                .vocCode(vocDTO.getVocCode())
                .vocContent(vocDTO.getVocContent())
                .createdAt(vocDTO.getCreatedAt())
                .vocAnswerStatus(vocDTO.getVocAnswerStatus())
                .vocAnswerSatisfaction(vocDTO.getVocAnswerSatisfaction())
                .createdAt(vocDTO.getCreatedAt())
                .vocCategory(vocCategory)
                .member(member)
                .build();
    }

    public VOCFilterRequestDTO fromFilterVOtoFilterDTO(RequestFilterVOCVO request) {
        return VOCFilterRequestDTO.builder()
                .vocCode(request.getVocCode())
                .vocContent(request.getVocContent())
                .vocCategoryCode(request.getVocCategoryCode())
                .memberType(request.getMemberType())
                .vocAnswerStatus(request.getVocAnswerStatus())
                .vocAnswerSatisfaction(Boolean.FALSE.equals(request.getVocAnswerStatus()) ? null : request.getVocAnswerSatisfaction())
                .startCreateDate(request.getStartCreateDate())
                .startEndDate(request.getStartEndDate())
                .build();
    }

    public ResponseFindVOCVO fromDTOToResponseVOAll(VOCDTO vocDTO, MemberDTO memberDTO, VOCCategoryDTO categoryDTO, AdminDTO adminDTO) {
        return ResponseFindVOCVO.builder()
                .vocCode(vocDTO.getVocCode())
                .vocContent(vocDTO.getVocContent())
                .vocCategoryName(categoryDTO.getVocCategoryName())
                .memberType(String.valueOf(memberDTO.getMemberType()))
                .memberName(memberDTO.getMemberName())
                .memberCode(memberDTO.getMemberCode())
                .adminName(adminDTO != null ? adminDTO.getAdminName() : "-")
                .createdAt(vocDTO.getCreatedAt())
                .vocAnswerStatus(vocDTO.getVocAnswerStatus())
                .vocAnswerSatisfaction(vocDTO.getVocAnswerSatisfaction())
                .build();
    }

    public ResponseFindVOCVO toUnansweredVOCResponseVO(VOCDTO vocDTO, MemberDTO memberDTO, VOCCategoryDTO categoryDTO) {
        return ResponseFindVOCVO.builder()
                .vocCode(vocDTO.getVocCode())
                .vocContent(vocDTO.getVocContent())
                .vocCategoryName(categoryDTO.getVocCategoryName())
                .memberType(String.valueOf(memberDTO.getMemberType()))
                .memberName(memberDTO.getMemberName())
                .memberCode(memberDTO.getMemberCode())
                .adminName(null)
                .createdAt(vocDTO.getCreatedAt())
                .vocAnswerStatus(null)
                .vocAnswerSatisfaction(null)
                .build();
    }
}
