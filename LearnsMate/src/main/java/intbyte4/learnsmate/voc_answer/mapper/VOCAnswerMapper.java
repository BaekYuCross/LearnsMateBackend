package intbyte4.learnsmate.voc_answer.mapper;

import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.voc.domain.VOC;
import intbyte4.learnsmate.voc_answer.domain.VOCAnswer;
import intbyte4.learnsmate.voc_answer.domain.dto.VOCAnswerDTO;
import intbyte4.learnsmate.voc_answer.domain.vo.request.RequestEditVOCAnswerVO;
import intbyte4.learnsmate.voc_answer.domain.vo.request.RequestRegisterVOCAnswerVO;
import intbyte4.learnsmate.voc_answer.domain.vo.response.ResponseEditVOCAnswerVO;
import intbyte4.learnsmate.voc_answer.domain.vo.response.ResponseFindVOCAnswerVO;
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

    public VOCAnswerDTO fromEditRequestVOToDto(RequestEditVOCAnswerVO request) {
        return  VOCAnswerDTO.builder()
                .vocAnswerContent(request.getVocAnswerContent())
                .build();
    }

    public ResponseRegisterVOCAnswerVO fromDTOToRegisterResponseVO(VOCAnswerDTO registerVocAnswer) {
        return ResponseRegisterVOCAnswerVO.builder()
                .vocAnswerCode(registerVocAnswer.getVocAnswerCode())
                .vocAnswerContent(registerVocAnswer.getVocAnswerContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .vocCode(registerVocAnswer.getVocCode())
                .adminCode(registerVocAnswer.getAdminCode())
                .build();
    }

    public ResponseEditVOCAnswerVO fromDTOToEditResponseVO(VOCAnswerDTO updatedTemplateDTO) {
        return ResponseEditVOCAnswerVO.builder()
                .vocAnswerContent(updatedTemplateDTO.getVocAnswerContent())
                .updatedAt(updatedTemplateDTO.getUpdatedAt())
                .build();
    }

    public VOCAnswer toEntity(VOCAnswerDTO vocAnswerDTO, Admin user, VOC voc) {
        return VOCAnswer.builder()
                .vocAnswerCode(vocAnswerDTO.getVocAnswerCode())
                .vocAnswerContent(vocAnswerDTO.getVocAnswerContent())
                .voc(voc)
                .admin(user)
                .build();
    }

    public ResponseFindVOCAnswerVO fromDTOToFindResponseVO(VOCAnswerDTO vocAnswerDTO) {
        return ResponseFindVOCAnswerVO.builder()
                .vocAnswerCode(vocAnswerDTO.getVocAnswerCode())
                .vocAnswerContent(vocAnswerDTO.getVocAnswerContent())
                .vocCode(vocAnswerDTO.getVocCode())
                .adminCode(vocAnswerDTO.getAdminCode())
                .createdAt(vocAnswerDTO.getCreatedAt())
                .updatedAt(vocAnswerDTO.getUpdatedAt())
                .build();
    }
}
