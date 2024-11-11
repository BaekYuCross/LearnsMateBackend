package intbyte4.learnsmate.lecture_category_by_lecture.service;

import intbyte4.learnsmate.lecture_category_by_lecture.mapper.LectureCategoryByLectureMapper;
import intbyte4.learnsmate.lecture_category_by_lecture.repository.LectureCategoryByLectureRepository;
import org.springframework.stereotype.Service;

@Service
public class LectureCategoryByLectureService {

    private LectureCategoryByLectureRepository lectureCategoryByLectureRepository;
    private LectureCategoryByLectureMapper lectureCategoryByLectureMapper;
}
