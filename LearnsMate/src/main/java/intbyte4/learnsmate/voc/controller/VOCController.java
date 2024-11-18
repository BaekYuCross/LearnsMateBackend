package intbyte4.learnsmate.voc.controller;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import intbyte4.learnsmate.voc.domain.vo.reqeust.RequestCountByCategoryVO;
import intbyte4.learnsmate.voc.domain.vo.response.ResponseCountByCategoryVO;
import intbyte4.learnsmate.voc.domain.vo.response.ResponseFindVOCVO;
import intbyte4.learnsmate.voc.service.VOCFacade;
import intbyte4.learnsmate.voc.service.VOCService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController("vocController")
@RequestMapping("voc")
@Slf4j
@RequiredArgsConstructor
public class VOCController {

    private final VOCService vocService;
    private final VOCFacade vocFacade;

    @Operation(summary = "직원 - VOC 전체 조회")
    @GetMapping("/list")
    public ResponseEntity<List<ResponseFindVOCVO>> listVOC() {
        List<ResponseFindVOCVO> response = vocFacade.findAllVOCs();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "직원 - VOC 단 건 조회")
    @GetMapping("/{vocCode}")
    public ResponseEntity<?> getVOC(@PathVariable("vocCode") Long vocCode) {
        log.info("조회 요청된 VOC 코드 : {}", vocCode);
        try {
            ResponseFindVOCVO response = vocFacade.findVOC(vocCode);
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

    @Operation(summary = "직원 - 특정 유저 미답변 VOC 리스트 조회")
    @GetMapping("/{memberCode}/unanswered")
    public ResponseEntity<List<ResponseFindVOCVO>> listUnansweredVOC(@PathVariable("memberCode") Long memberCode) {
        log.info("특정 유저 미답변 VOC 조회 요청: userCode = {}", memberCode);
        List<VOCDTO> vocDTOList = vocService.findUnansweredVOCByMember(memberCode);
        List<ResponseFindVOCVO> responseList = new ArrayList<>();
        for (VOCDTO vocdto : vocDTOList) {
            ResponseFindVOCVO response = vocFacade.findVOC(vocdto.getVocCode());
            responseList.add(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @Operation(summary = "직원 - 특정 유저 답변 VOC 리스트 조회")
    @GetMapping("/{memberCode}/answered")
    public ResponseEntity<List<ResponseFindVOCVO>> listAnsweredVOC(@PathVariable("memberCode") Long memberCode) {
        log.info("특정 유저 답변 VOC 조회 요청: userCode = {}", memberCode);
        List<VOCDTO> vocDTOList = vocService.findAnsweredVOCByMember(memberCode);
        List<ResponseFindVOCVO> responseList = new ArrayList<>();
        for (VOCDTO vocdto : vocDTOList) {
            ResponseFindVOCVO response = vocFacade.findVOC(vocdto.getVocCode());
            responseList.add(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @Operation(summary = "직원 - 기간 별 VOC 카테고리 별 개수 조회")
    @GetMapping("/count-by-category")
    public ResponseEntity<List<ResponseCountByCategoryVO>> countVOCByCategory(@RequestBody RequestCountByCategoryVO requestVO) {
        Map<Integer, Long> categoryCountMap = vocService.countVOCByCategory(requestVO.getStartDate(), requestVO.getEndDate());
        List<ResponseCountByCategoryVO> response = categoryCountMap.entrySet().stream()
                .map(entry -> ResponseCountByCategoryVO.builder()
                        .vocCategoryCode(entry.getKey())
                        .vocCount(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "VOC 필터링")
    @PostMapping("/filter")
    public ResponseEntity<List<VOCDTO>> filterVOC(@RequestBody VOCDTO vocDTO, MemberDTO memberDTO) {
        log.info("VOC 필터링 요청 수신");
        try {
            List<VOCDTO> filteredVOCList = vocService.filterVOC(vocDTO, memberDTO);

            log.info("VOC 필터링 성공, 필터링된 데이터 수: {}", filteredVOCList.size());
            return ResponseEntity.ok(filteredVOCList);
        } catch (CommonException e) {
            log.error("VOC 필터링 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
