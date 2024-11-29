package intbyte4.learnsmate.lecture.controller;

import intbyte4.learnsmate.lecture.domain.dto.*;
import intbyte4.learnsmate.lecture.domain.vo.request.RequestLectureFilterVO;
import intbyte4.learnsmate.lecture.domain.vo.response.*;
import intbyte4.learnsmate.lecture.pagination.LecturePaginationResponse;
import intbyte4.learnsmate.lecture.service.LectureFacade;
import intbyte4.learnsmate.lecture.domain.vo.request.RequestEditLectureInfoVO;
import intbyte4.learnsmate.lecture.domain.vo.request.RequestRegisterLectureVO;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.video_by_lecture.domain.dto.VideoByLectureDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lecture")
@Slf4j
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;
    private final LectureMapper lectureMapper;
    private final LectureFacade lectureFacade;

    @Operation(summary = "학생이 강의를 클릭할 때 클릭 수 증가")
    @PatchMapping("/{lectureCode}/click")
    public ResponseEntity<?> incrementClickCount(@PathVariable("lectureCode") String lectureCode) {
        try {
            lectureService.incrementClickCount(lectureCode);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "강의 정보 페이지네이션 방식으로 전체 조회")
    @GetMapping("/list")
    public ResponseEntity<LecturePaginationResponse<ResponseFindLectureVO>> getLecturesWithPagination(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        LecturePaginationResponse<ResponseFindLectureVO> response = lectureFacade.getLecturesWithPaginationByOffset(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "강의 단 건 조회 및 구매 전환율 계산")
    @GetMapping("/{lectureCode}")
    public ResponseEntity<ResponseFindLectureDetailVO> getLecture(@PathVariable("lectureCode") String lectureCode) {
        ResponseFindLectureDetailVO lectureDetail = lectureFacade.getLectureDetailsWithConversionRates(lectureCode);
        return ResponseEntity.status(HttpStatus.OK).body(lectureDetail);
    }

    @Operation(summary = "강의 통계 및 날짜 필터 적용")
    @PostMapping("/{lectureCode}/stats/filter")
    public ResponseEntity<LectureStatsVO> getLectureStatsWithFilter(@PathVariable("lectureCode") String lectureCode, @RequestBody LectureStatsFilterDTO filterDTO) {
        LectureStatsVO stats = lectureFacade.getLectureStatsWithFilterAndRates(lectureCode, filterDTO);
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "강의와 강의별 동영상 등록 요청")
    @PostMapping("/register")
    public ResponseEntity<ResponseRegisterLectureVO> registerLecture(@RequestBody RequestRegisterLectureVO registerLectureVO) {
        LectureDTO lectureDTO = lectureMapper.fromRegisterRequestVOtoDto(registerLectureVO);
        List<Integer> lectureCategoryCodeList = registerLectureVO.getLectureCategoryCodeList();
        List<VideoByLectureDTO> videoByLectureDTOList = registerLectureVO.getVideoByLectureDTOList();

        LectureDTO registeredLectureDTO = lectureFacade.registerLecture(lectureDTO, lectureCategoryCodeList, videoByLectureDTOList);

        return ResponseEntity.status(HttpStatus.CREATED).body(lectureMapper.fromDtoToRegisterResponseVO(registeredLectureDTO));
    }

    @Operation(summary = "강의 수정")
    @PatchMapping("/{lectureCode}/info")
    public ResponseEntity<ResponseEditLectureInfoVO> updateLecture(@RequestBody RequestEditLectureInfoVO requestVO, @PathVariable String lectureCode) {
        LectureDTO lectureDTO = lectureMapper.fromRequestVOtoDto(requestVO);
        lectureDTO.setLectureCode(lectureCode);

        LectureDTO updatedLecture = lectureFacade.updateLecture(lectureDTO, requestVO.getNewVideoTitle(), requestVO.getNewVideoLink(), requestVO.getLectureCategoryCodeList());

        ResponseEditLectureInfoVO responseVO = lectureMapper.fromDtoToEditResponseVO(updatedLecture);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseVO);
    }

    @Operation(summary = "강의 삭제")
    @PatchMapping("/{lectureCode}/status")
    public ResponseEntity<ResponseRemoveLectureVO> removeLecture(@PathVariable("lectureCode")  String lectureCode) {
        LectureDTO removedLecture = lectureFacade.removeLecture(lectureCode);
        return ResponseEntity.status(HttpStatus.OK).body(lectureMapper.fromDtoToRemoveResponseVO(removedLecture));
    }

    @Operation(summary = "전체 조회 화면에서의 필터링 기능")
    @PostMapping("/filter")
    public ResponseEntity<LecturePaginationResponse<ResponseFindLectureVO>> filterLectures(@RequestBody RequestLectureFilterVO request, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        try {
            LectureFilterDTO filterDTO = lectureMapper.toFilterDTO(request);
            log.info("filterDTO: " + filterDTO.toString());
            LecturePaginationResponse<ResponseFindLectureVO> response = lectureFacade.filterLecturesByPage(filterDTO, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("강의 필터링 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "월별/연도별 전체 강의 수 조회")
    @GetMapping("/monthly-counts")
    public ResponseEntity<List<MonthlyLectureCountDTO>> getMonthlyLectureCounts() {
        List<MonthlyLectureCountDTO> lectureCounts = lectureService.getMonthlyLectureCounts();
        return ResponseEntity.ok(lectureCounts);
    }

    @Operation(summary = "기간별 월별 강의 수 조회")
    @PostMapping("/monthly-counts/filter")
    public ResponseEntity<List<MonthlyLectureCountDTO>> getFilteredMonthlyLectureCounts(
            @RequestBody MonthlyLectureFilterDTO filterDTO) {
        List<MonthlyLectureCountDTO> lectureCounts = lectureService.getFilteredMonthlyLectureCounts(filterDTO);
        return ResponseEntity.ok(lectureCounts);
    }
}
