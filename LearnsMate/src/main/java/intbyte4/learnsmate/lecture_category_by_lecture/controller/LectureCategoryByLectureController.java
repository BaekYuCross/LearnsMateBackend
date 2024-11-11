package intbyte4.learnsmate.lecture_category_by_lecture.controller;

import intbyte4.learnsmate.lecture_category_by_lecture.domain.dto.LectureCategoryByLectureDTO;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.vo.response.ResponseFindLectureCategoryByLectureVO;
import intbyte4.learnsmate.lecture_category_by_lecture.mapper.LectureCategoryByLectureMapper;
import intbyte4.learnsmate.lecture_category_by_lecture.service.LectureCategoryByLectureService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lecturecategorybylecture")
public class LectureCategoryByLectureController {

    private final LectureCategoryByLectureService lectureCategoryByLectureService;
    private final LectureCategoryByLectureMapper lectureCategoryByLectureMapper;

    // 1. 모든 강의별 강의 카테고리 조회
    @Operation(summary = "모든 강의별 강의 카테고리 조회")
    @GetMapping
    public ResponseEntity<?> findAll(){
        List<LectureCategoryByLectureDTO> dtoList = lectureCategoryByLectureService.findAll();

        List<ResponseFindLectureCategoryByLectureVO> voList = lectureCategoryByLectureMapper.fromDTOtoFindVO(dtoList);

        return ResponseEntity.status(HttpStatus.OK).body(voList);
    }
}
