package intbyte4.learnsmate.video_by_lecture.controller;

import intbyte4.learnsmate.video_by_lecture.domain.dto.VideoByLectureDTO;
import intbyte4.learnsmate.video_by_lecture.domain.vo.response.ResponseFindVideoByLectureVO;
import intbyte4.learnsmate.video_by_lecture.mapper.VideoByLectureMapper;
import intbyte4.learnsmate.video_by_lecture.service.VideoByLectureService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class VideoByLectureController {

    private final VideoByLectureService videoByLectureService;
    private final VideoByLectureMapper videoByLectureMapper;

    @Operation(summary = "강의코드별 모든 강의별 동영상 조회")
    @GetMapping("/lectures/{lectureCode}/videos")
    public ResponseEntity<List<ResponseFindVideoByLectureVO>> findVideoByLectureByLectureCode(
            @PathVariable("lectureCode") Long lectureCode) {

        List<VideoByLectureDTO> videoByLectureDTOs = videoByLectureService.findVideoByLectureByLectureCode(lectureCode);
        List<ResponseFindVideoByLectureVO> response = videoByLectureDTOs.stream()
                .map(videoByLectureMapper::toResponseVO)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
