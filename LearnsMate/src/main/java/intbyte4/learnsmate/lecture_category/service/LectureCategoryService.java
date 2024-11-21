package intbyte4.learnsmate.lecture_category.service;

import intbyte4.learnsmate.lecture_category.domain.dto.LectureCategoryDTO;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategoryEnum;

public interface LectureCategoryService {
    LectureCategoryDTO findByLectureCategoryCode(Integer lectureCategoryCode);

    LectureCategoryEnum findLectureCategoryNameByCode(Integer lectureCategoryCode);
}
