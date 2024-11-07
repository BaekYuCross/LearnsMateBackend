package intbyte4.learnsmate.voc.mapper;

import intbyte4.learnsmate.voc.domain.Voc;
import intbyte4.learnsmate.voc.domain.dto.VocDTO;
import intbyte4.learnsmate.voc.domain.vo.response.ResponseFindVocVO;
import org.springframework.stereotype.Component;

@Component
public class VocMapper {
    public VocDTO fromEntityToDto(Voc voc) {
        return VocDTO.builder()
                .vocCode(voc.getVocCode())
                .vocContent(voc.getVocContent())
                .vocAnswerStatus(voc.getVocAnswerStatus())
                .vocAnswerSatisfaction(voc.getVocAnswerSatisfaction())
                .vocAnalysis(voc.getVocAnalysis())
                .vocCategoryCode(voc.getVocCategory().getVocCategoryCode())
                .memberCode(voc.getMember().getMemberCode())
                .build();
    }

    public ResponseFindVocVO fromDtoToFindResponseVO(VocDTO vocDTO) {
        return ResponseFindVocVO.builder()
                .vocCode(vocDTO.getVocCode())
                .vocContent(vocDTO.getVocContent())
                .vocAnswerStatus(vocDTO.getVocAnswerStatus())
                .vocAnswerSatisfaction(vocDTO.getVocAnswerSatisfaction())
                .vocAnalysis(vocDTO.getVocAnalysis())
                .vocCategoryCode(vocDTO.getVocCategoryCode())
                .memberCode(vocDTO.getMemberCode())
                .build();
    }
}
