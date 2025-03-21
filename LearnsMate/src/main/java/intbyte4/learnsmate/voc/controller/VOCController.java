package intbyte4.learnsmate.voc.controller;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.voc.domain.dto.*;
import intbyte4.learnsmate.voc.domain.vo.request.RequestCountByCategoryVO;
import intbyte4.learnsmate.voc.domain.vo.request.RequestFilterVOCVO;
import intbyte4.learnsmate.voc.domain.vo.request.RequestSaveVOCVO;
import intbyte4.learnsmate.voc.domain.vo.request.VOCCategoryRatioFilterRequest;
import intbyte4.learnsmate.voc.domain.vo.response.ResponseCountByCategoryVO;
import intbyte4.learnsmate.voc.domain.vo.response.ResponseFindClientVOCVO;
import intbyte4.learnsmate.voc.domain.vo.response.ResponseFindVOCVO;
import intbyte4.learnsmate.voc.mapper.VOCMapper;
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
    private final VOCMapper vocMapper;

    @Operation(summary = "직원 - VOC 페이지 조회")
    @GetMapping("/list")
    public ResponseEntity<VOCPageResponse<ResponseFindVOCVO>> listVOC(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size) {
        VOCPageResponse<ResponseFindVOCVO> response = vocFacade.findVOCsByPage(page, size);
        return ResponseEntity.ok(response);
    }

    // 필터링x 정렬o
    @Operation(summary = "직원 - VOC 페이지 조회")
    @GetMapping("/list/sort")
    public ResponseEntity<VOCPageResponse<ResponseFindVOCVO>> listVOCWithSort(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortField,
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection) {
        log.info("{}{}{}{}", page, size, sortField, sortDirection);
        VOCPageResponse<ResponseFindVOCVO> response = vocFacade.findVOCsByPageWithSort(page, size, sortField, sortDirection);
        log.info(response.toString());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "직원 - VOC 단 건 조회")
    @GetMapping("/{vocCode}")
    public ResponseEntity<?> getVOC(@PathVariable("vocCode") String vocCode) {
        try {
            ResponseFindVOCVO response = vocFacade.findVOC(vocCode);
            log.info("VOC 단 건 조회 성공: {}", response);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CommonException e) {
            log.error("VOC 단 건 조회 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다");
        }
    }

    @Operation(summary = "직원 - 특정 유저 미답변 VOC 리스트 조회")
    @GetMapping("/{memberCode}/unanswered")
    public ResponseEntity<List<ResponseFindVOCVO>> listUnansweredVOC(@PathVariable("memberCode") Long memberCode) {
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
        List<VOCDTO> vocDTOList = vocService.findAnsweredVOCByMember(memberCode);
        List<ResponseFindVOCVO> responseList = new ArrayList<>();
        for (VOCDTO vocdto : vocDTOList) {
            ResponseFindVOCVO response = vocFacade.findVOC(vocdto.getVocCode());
            responseList.add(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @Operation(summary = "직원 - 전체 유저 미답변 VOC 리스트 조회")
    @GetMapping("/unanswered")
    public ResponseEntity<List<ResponseFindVOCVO>> AllUnansweredVOC() {
        List<VOCDTO> vocDTOList = vocService.findUnansweredVOC();
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
    public ResponseEntity<VOCPageResponse<ResponseFindVOCVO>> filterVOC(@RequestBody RequestFilterVOCVO request, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "30") int size) {
        try {
            VOCFilterRequestDTO dto = vocMapper.fromFilterVOtoFilterDTO(request);
            VOCPageResponse<ResponseFindVOCVO> response = vocFacade.filterVOCsByPage(dto, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 필터링o 정렬o
    @Operation(summary = "VOC 필터링")
    @PostMapping("/filter/sort")
    public ResponseEntity<VOCPageResponse<ResponseFindVOCVO>> filterVOCWithSort(
            @RequestBody RequestFilterVOCVO request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortField,
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection) {
        log.info("VOC 컬럼 필터링 요청 수신");
        log.info("{}{}{}{}", page, size, sortField, sortDirection);
        try {
            VOCFilterRequestDTO dto = vocMapper.fromFilterVOtoFilterDTO(request);
            VOCPageResponse<ResponseFindVOCVO> response = vocFacade.filterVOCsByPageWithSort(dto, page, size, sortField, sortDirection);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "전체 VOC 카테고리별 건수 조회")
    @GetMapping("/category-count")
    public ResponseEntity<List<VOCCategoryCountDTO>> getCategoryCounts() {
        List<VOCCategoryCountDTO> categoryCounts = vocFacade.getCategoryCounts();
        return ResponseEntity.ok(categoryCounts);
    }

    @Operation(summary = "특정 기간 VOC 카테고리별 건수 조회")
    @PostMapping("/category-count/filter")
    public ResponseEntity<List<VOCCategoryCountDTO>> getFilteredCategoryCounts(@RequestBody VOCCategoryRatioFilterRequest request) {
        List<VOCCategoryCountDTO> filteredCategoryCounts = vocFacade.getFilteredCategoryCounts(request.getStartDate(), request.getEndDate());
        return ResponseEntity.ok(filteredCategoryCounts);
    }

    @Operation(summary = "학생 - VOC 등록")
    @PostMapping
    public ResponseEntity<?> saveVOC(@RequestBody RequestSaveVOCVO request) {

        VOCDTO dto = vocMapper.fromRequestSaveVOCVOtoVOCDTO(request);

        VOCDTO responseDTO = vocFacade.saveVOC(dto);

        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "학생 - 자신이 작성한 전체 VOC 조회")
    @GetMapping("/client/list/{memberCode}")
    public ResponseEntity<?> findClientVOC(@PathVariable("memberCode") Long memberCode) {
        List<VOCClientDTO> dtoList = vocService.findAllClientVOC(memberCode);
        List<ResponseFindClientVOCVO> voList = vocMapper.fromDTOtoResponseFindClientVOCVO(dtoList);
        return ResponseEntity.ok(voList);
    }

    @Operation(summary = "학생 - VOC 만족도 평가")
    @PostMapping("/client/{vocCode}/feedback")
    public ResponseEntity<String> updateVocSatisfaction(
            @PathVariable("vocCode") String vocCode,
            @RequestParam("satisfaction") Long satisfaction
    ){
        vocService.updateVocSatisfaction(vocCode, satisfaction);

        return ResponseEntity.ok("만족도 업데이트 완료");
    }
}
