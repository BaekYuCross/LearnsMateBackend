package intbyte4.learnsmate.voc.controller;

import intbyte4.learnsmate.common.exception.CommonException;
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
import org.springframework.web.bind.annotation.PathVariable;
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

    @Operation(summary = "직원 - VOC 단 건 조회")
    @GetMapping("/{vocCode}")
    public ResponseEntity<?> getTemplate(@PathVariable("vocCode") Long vocCode) {
        log.info("조회 요청된 VOC 코드 : {}", vocCode);
        try {
            VocDTO vocDTO = new VocDTO();
            vocDTO.setVocCode(vocCode);

            VocDTO findVocDTO = vocService.findByVocCode(vocDTO);
            ResponseFindVocVO response = vocMapper.fromDtoToFindResponseVO(findVocDTO);

            log.info("캠페인 템플릿 조회 성공: {}", response);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CommonException e) {
            log.error("캠페인 템플릿 조회 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다");
        }
    }
}
