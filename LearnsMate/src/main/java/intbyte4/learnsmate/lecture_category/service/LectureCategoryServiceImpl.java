package intbyte4.learnsmate.lecture_category.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.lecture_category.domain.dto.LectureCategoryDTO;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategory;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategoryEnum;
import intbyte4.learnsmate.lecture_category.mapper.LectureCategoryMapper;
import intbyte4.learnsmate.lecture_category.repository.LectureCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LectureCategoryServiceImpl implements LectureCategoryService {
    private final LectureCategoryRepository lectureCategoryRepository;
    private final LectureCategoryMapper lectureCategoryMapper;

    @Override
    public LectureCategoryDTO findByLectureCategoryCode(Integer lectureCategoryCode) {

        LectureCategory lectureCategory = lectureCategoryRepository.findById(lectureCategoryCode)
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_CATEGORY_NOT_FOUND));

        return lectureCategoryMapper.toDTO(lectureCategory);
    }

    @Override
    public LectureCategoryEnum findLectureCategoryNameByCode(Integer lectureCategoryCode) {
        return lectureCategoryRepository.findById(lectureCategoryCode)
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_CATEGORY_NOT_FOUND))
                .getLectureCategoryName();
    }
}
