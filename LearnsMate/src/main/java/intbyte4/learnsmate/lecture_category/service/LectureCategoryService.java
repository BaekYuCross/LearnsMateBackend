package intbyte4.learnsmate.lecture_category.service;

import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategory;

public interface LectureCategoryService {
    LectureCategory findByLectureCategoryCode(Integer lectureCategoryCode);
}
