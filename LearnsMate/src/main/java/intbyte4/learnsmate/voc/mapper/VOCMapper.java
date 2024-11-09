package intbyte4.learnsmate.voc.mapper;

import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.voc.domain.VOC;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import intbyte4.learnsmate.voc.domain.vo.response.ResponseFindVOCVO;
import intbyte4.learnsmate.voc_category.domain.VocCategory;
import org.springframework.stereotype.Component;

@Component
public class VOCMapper {
    public VOCDTO fromEntityToDTO(VOC voc) {
        return VOCDTO.builder()
                .vocCode(voc.getVocCode())
                .vocContent(voc.getVocContent())
                .vocAnswerStatus(voc.getVocAnswerStatus())
                .vocAnswerSatisfaction(voc.getVocAnswerSatisfaction())
                .vocAnalysis(voc.getVocAnalysis())
                .vocCategoryCode(voc.getVocCategory().getVocCategoryCode())
                .memberCode(voc.getMember().getMemberCode())
                .build();
    }

    public ResponseFindVOCVO fromDTOToResponseVO(VOCDTO vocDTO) {
        return ResponseFindVOCVO.builder()
                .vocCode(vocDTO.getVocCode())
                .vocContent(vocDTO.getVocContent())
                .vocAnswerStatus(vocDTO.getVocAnswerStatus())
                .vocAnswerSatisfaction(vocDTO.getVocAnswerSatisfaction())
                .vocAnalysis(vocDTO.getVocAnalysis())
                .vocCategoryCode(vocDTO.getVocCategoryCode())
                .memberCode(vocDTO.getMemberCode())
                .build();
    }

    public VOC toEntity(VOCDTO vocDTO, VocCategory vocCategory, Member member) {
        return VOC.builder()
                .vocCode(vocDTO.getVocCode())
                .vocContent(vocDTO.getVocContent())
                .vocAnswerStatus(vocDTO.getVocAnswerStatus())
                .vocAnswerSatisfaction(vocDTO.getVocAnswerSatisfaction())
                .vocAnalysis(vocDTO.getVocAnalysis())
                .createdAt(vocDTO.getCreatedAt())
                .vocCategory(vocCategory)
                .member(member)
                .build();
    }
}
