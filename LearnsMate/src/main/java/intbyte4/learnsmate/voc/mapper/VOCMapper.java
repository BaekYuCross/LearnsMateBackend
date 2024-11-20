package intbyte4.learnsmate.voc.mapper;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.voc.domain.VOC;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import intbyte4.learnsmate.voc.domain.vo.response.ResponseFindVOCVO;
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

    public ResponseFindVOCVO fromDTOToResponseVO(VOCDTO vocDTO, MemberDTO memberDTO, VOCCategoryDTO categoryDTO, AdminDTO adminDTO) {
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
}
