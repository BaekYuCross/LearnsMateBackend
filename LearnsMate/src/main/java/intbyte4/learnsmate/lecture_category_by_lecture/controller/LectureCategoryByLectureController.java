package intbyte4.learnsmate.lecture_category_by_lecture.controller;

import intbyte4.learnsmate.lecture_category_by_lecture.service.LectureCategoryByLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LectureCategoryByLectureController {

    private final LectureCategoryByLectureService lectureCategoryByLectureService;
}
