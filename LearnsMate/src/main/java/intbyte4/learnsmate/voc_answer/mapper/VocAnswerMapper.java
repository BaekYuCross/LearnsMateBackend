package intbyte4.learnsmate.voc_answer.mapper;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.voc.domain.Voc;
import intbyte4.learnsmate.voc_answer.domain.VocAnswer;
import intbyte4.learnsmate.voc_answer.domain.dto.VocAnswerDTO;
import intbyte4.learnsmate.voc_answer.domain.vo.request.RequestRegisterVocAnswerVO;
import intbyte4.learnsmate.voc_answer.domain.vo.response.ResponseRegisterVocAnswerVO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class VocAnswerMapper {
    public VocAnswerDTO fromEntityToDto(VocAnswer savedVocAnswer) {
        return VocAnswerDTO.builder()
                .vocAnswerCode(savedVocAnswer.getVocAnswerCode())
                .vocAnswerContent(savedVocAnswer.getVocAnswerContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .vocCode(savedVocAnswer.getVoc().getVocCode())
                .adminCode(savedVocAnswer.getAdmin().getAdminCode())
                .build();
    }

    public VocAnswerDTO fromRegisterRequestVOToDto(RequestRegisterVocAnswerVO request) {
        return VocAnswerDTO.builder()
                .vocAnswerCode(request.getVocAnswerCode())
                .vocAnswerContent(request.getVocAnswerContent())
                .vocCode(request.getVocCode())
                .adminCode(request.getAdminCode())
                .build();
    }

    public ResponseRegisterVocAnswerVO fromDtoToRegisterResponseVO(VocAnswerDTO registerVocAnswer) {
        return ResponseRegisterVocAnswerVO.builder()
                .vocAnswerCode(registerVocAnswer.getVocAnswerCode())
                .vocAnswerContent(registerVocAnswer.getVocAnswerContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .vocCode(registerVocAnswer.getVocCode())
                .adminCode(registerVocAnswer.getAdminCode())
                .build();
    }

    public VocAnswer toEntity(VocAnswerDTO vocAnswerDTO, Admin user, Voc voc) {
        return VocAnswer.builder()
                .vocAnswerCode(vocAnswerDTO.getVocAnswerCode())
                .vocAnswerContent(vocAnswerDTO.getVocAnswerContent())
                .voc(voc)
                .admin(user)
                .build();
    }
}
