package intbyte4.learnsmate.voc.controller;

import intbyte4.learnsmate.voc.domain.dto.VocDTO;
import intbyte4.learnsmate.voc.domain.vo.response.ResponseFindVocVO;
import intbyte4.learnsmate.voc.mapper.VocMapper;
import intbyte4.learnsmate.voc.service.VocService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("vocController")
@RequestMapping("voc")
@Slf4j
@RequiredArgsConstructor
public class VocController {

    private final VocService vocService;
    private final VocMapper vocMapper;

    @Operation(summary = "직원 - VOC 전체 조회")
    @GetMapping("/list")
    public ResponseEntity<List<ResponseFindVocVO>> listTemplates() {
        List<VocDTO> vocDTOList = vocService.findAllByVoc();
        List<ResponseFindVocVO> responseList = new ArrayList<>();

        for (VocDTO vocDTO : vocDTOList) {
            ResponseFindVocVO responseFindTemplateVO = vocMapper.fromDtoToFindResponseVO(vocDTO);
            responseList.add(responseFindTemplateVO);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
