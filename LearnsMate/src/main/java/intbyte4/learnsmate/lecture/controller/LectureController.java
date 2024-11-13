package intbyte4.learnsmate.lecture.controller;

import intbyte4.learnsmate.lecture.service.LectureFacade;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.dto.LectureDetailDTO;
import intbyte4.learnsmate.lecture.domain.vo.request.RequestEditLectureInfoVO;
import intbyte4.learnsmate.lecture.domain.vo.request.RequestRegisterLectureVO;
import intbyte4.learnsmate.lecture.domain.vo.response.ResponseEditLectureInfoVO;
import intbyte4.learnsmate.lecture.domain.vo.response.ResponseFindLectureVO;
import intbyte4.learnsmate.lecture.domain.vo.response.ResponseRegisterLectureVO;
import intbyte4.learnsmate.lecture.domain.vo.response.ResponseRemoveLectureVO;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.service.LectureService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lecture")
@Slf4j
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;
    private final LectureFacade LectureFacade;
    private final LectureMapper lectureMapper;
    private final LectureFacade lectureFacade;

    @Operation(summary = "강의 정보 전체 조회")
    @GetMapping
    public ResponseEntity<List<ResponseFindLectureVO>> getAllLectures() {
        List<LectureDetailDTO> lectureDTOs = LectureFacade.getAllLecture();
        List<ResponseFindLectureVO> lectureVOs = lectureDTOs.stream()
                .map(lectureMapper::fromDtoToResponseVO)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(lectureVOs);
    }

    @Operation(summary = "강의 단건 조회")
    @GetMapping("/{lectureCode}")
    public ResponseEntity<ResponseFindLectureVO> getLecture(@PathVariable("lectureCode") Long lectureCode) {
        LectureDetailDTO lectureDTO = LectureFacade.getLectureById(lectureCode);
        return ResponseEntity.status(HttpStatus.OK).body(lectureMapper.fromDtoToResponseVO(lectureDTO));
    }

    @Operation(summary = "강의와 강의별 강의 카테고리 등록")
    @PutMapping("/{lectureCode}")
    public ResponseEntity<ResponseRegisterLectureVO> registerLecture(
            @RequestBody RequestRegisterLectureVO registerLectureVO) {
        LectureDTO lectureDTO = LectureFacade.registerLecture(lectureMapper.fromRegisterRequestVOtoDto(registerLectureVO), registerLectureVO.getLectureCategoryCodeList());
        return ResponseEntity.status(HttpStatus.CREATED).body(lectureMapper.fromDtoToRegisterResponseVO(lectureDTO));
    }

    @Operation(summary = "강의 수정")
    @PatchMapping("/{lectureCode}/info")
    public ResponseEntity<ResponseEditLectureInfoVO> updateLecture(
            @RequestBody RequestEditLectureInfoVO requestVO) {
        LectureDTO updatedLecture = lectureService.updateLecture(lectureMapper.fromRequestVOtoDto(requestVO));
        return ResponseEntity.status(HttpStatus.CREATED).body(lectureMapper.fromDtoToEditResponseVO(updatedLecture));
    }


    @Operation(summary = "강의 삭제")
    @PatchMapping("/{lectureCode}/status")
    public ResponseEntity<ResponseRemoveLectureVO> removeLecture(@PathVariable("lectureCode")  Long lectureCode) {
        LectureDTO removedLecture = lectureFacade.removeLecture(lectureCode);
        return ResponseEntity.status(HttpStatus.OK).body(lectureMapper.fromDtoToRemoveResponseVO(removedLecture));
    }

}
