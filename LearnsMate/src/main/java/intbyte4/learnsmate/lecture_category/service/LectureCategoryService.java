package intbyte4.learnsmate.lecture_category.service;

import intbyte4.learnsmate.lecture_category.domain.dto.LectureCategoryDTO;

public interface LectureCategoryService {
    LectureCategoryDTO findByLectureCategoryCode(Integer lectureCategoryCode);

    LectureCategoryDTO findByLectureCode(Long lectureCode);
}
