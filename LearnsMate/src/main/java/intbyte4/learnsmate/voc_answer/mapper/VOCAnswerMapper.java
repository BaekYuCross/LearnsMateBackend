package intbyte4.learnsmate.voc_answer.mapper;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.voc.domain.Voc;
import intbyte4.learnsmate.voc_answer.domain.VOCAnswer;
import intbyte4.learnsmate.voc_answer.domain.dto.VOCAnswerDTO;
import intbyte4.learnsmate.voc_answer.domain.vo.request.RequestRegisterVOCAnswerVO;
import intbyte4.learnsmate.voc_answer.domain.vo.response.ResponseRegisterVOCAnswerVO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class VOCAnswerMapper {
    public VOCAnswerDTO fromEntityToDTO(VOCAnswer savedVOCAnswer) {
        return VOCAnswerDTO.builder()
                .vocAnswerCode(savedVOCAnswer.getVocAnswerCode())
                .vocAnswerContent(savedVOCAnswer.getVocAnswerContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .vocCode(savedVOCAnswer.getVoc().getVocCode())
                .adminCode(savedVOCAnswer.getAdmin().getAdminCode())
                .build();
    }

    public VOCAnswerDTO fromRegisterRequestVOToDTO(RequestRegisterVOCAnswerVO request) {
        return VOCAnswerDTO.builder()
                .vocAnswerCode(request.getVocAnswerCode())
                .vocAnswerContent(request.getVocAnswerContent())
                .vocCode(request.getVocCode())
                .adminCode(request.getAdminCode())
                .build();
    }

    public ResponseRegisterVOCAnswerVO fromDtoToRegisterResponseVO(VOCAnswerDTO registerVocAnswer) {
        return ResponseRegisterVOCAnswerVO.builder()
                .vocAnswerCode(registerVocAnswer.getVocAnswerCode())
                .vocAnswerContent(registerVocAnswer.getVocAnswerContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .vocCode(registerVocAnswer.getVocCode())
                .adminCode(registerVocAnswer.getAdminCode())
                .build();
    }

    public VOCAnswer toEntity(VOCAnswerDTO vocAnswerDTO, Admin user, Voc voc) {
        return VOCAnswer.builder()
                .vocAnswerCode(vocAnswerDTO.getVocAnswerCode())
                .vocAnswerContent(vocAnswerDTO.getVocAnswerContent())
                .voc(voc)
                .admin(user)
                .build();
    }
}
