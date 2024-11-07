package intbyte4.learnsmate.lecture.controller;

import intbyte4.learnsmate.campaign.domain.vo.request.RequestRegisterCampaignVO;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.vo.request.RequestEditLectureInfoVO;
import intbyte4.learnsmate.lecture.domain.vo.response.ResponseEditLectureInfoVO;
import intbyte4.learnsmate.lecture.domain.vo.response.ResponseFindLectureVO;
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
    private final LectureMapper lectureMapper;

    @Operation(summary = "강의 정보 전체 조회")
    @GetMapping
    public ResponseEntity<List<ResponseFindLectureVO>> getAllLectures() {
        List<LectureDTO> lectureDTOs = lectureService.getAllLecture();
        List<ResponseFindLectureVO> lectureVOs = lectureDTOs.stream()
                .map(lectureMapper::fromDtoToResponseVO)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(lectureVOs);
    }

    @Operation(summary = "강의 단건 조회")
    @GetMapping("/{lectureCode}")
    public ResponseEntity<ResponseFindLectureVO> getLecture(@PathVariable("lectureCode") Long lectureCode) {
        LectureDTO lectureDTO = lectureService.getLectureById(lectureCode);
        return ResponseEntity.status(HttpStatus.OK).body(lectureMapper.fromDtoToResponseVO(lectureDTO));
    }

    @Operation(summary = "강의 등록")
    @PutMapping("/{lectureCode}")
    public ResponseEntity<ResponseEditLectureInfoVO> registerLecture(
            @PathVariable("lectureCode")  Long lectureCode) {
        LectureDTO updatedLecture = lectureService.registerLecture(lectureCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(lectureMapper.fromDtoToEditResponseVO(updatedLecture));
    }


}
