package intbyte4.learnsmate.lecture_category_by_lecture.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.dto.LectureCategoryByLectureDTO;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.entity.LectureCategoryByLecture;
import intbyte4.learnsmate.lecture_category_by_lecture.mapper.LectureCategoryByLectureMapper;
import intbyte4.learnsmate.lecture_category_by_lecture.repository.LectureCategoryByLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LectureCategoryByLectureService {

    private final LectureCategoryByLectureRepository lectureCategoryByLectureRepository;
    private final LectureCategoryByLectureMapper lectureCategoryByLectureMapper;

    // 모든 강의별 강의 카테고리 조회 메서드
    public List<LectureCategoryByLectureDTO> findAll() {

        List<LectureCategoryByLecture> entityList = lectureCategoryByLectureRepository.findAll();

        return lectureCategoryByLectureMapper.fromEntitytoDTO(entityList);
    }

    // 특정 강의별 강의 카테고리 조회 메서드
    public LectureCategoryByLectureDTO findById(Long code) {
        LectureCategoryByLecture entity = lectureCategoryByLectureRepository.findById(code)
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_CATEGORY_BY_LECTURE_NOT_FOUND));

        return lectureCategoryByLectureMapper.fromEntitytoDTO(List.of(entity)).get(0);
    }
}
