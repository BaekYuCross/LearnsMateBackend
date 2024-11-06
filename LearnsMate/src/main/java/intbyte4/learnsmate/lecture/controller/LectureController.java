package intbyte4.learnsmate.lecture.controller;

import intbyte4.learnsmate.lecture.domain.vo.response.ResponseFindLectureVO;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.service.LectureService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        List<ResponseFindLectureVO> lectures = lectureService.getAllLecture();
        return new ResponseEntity<>(lectures, HttpStatus.OK);
    }

}
