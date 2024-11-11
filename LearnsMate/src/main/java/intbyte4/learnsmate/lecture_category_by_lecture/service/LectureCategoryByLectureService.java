package intbyte4.learnsmate.lecture_category_by_lecture.service;

import intbyte4.learnsmate.lecture_category_by_lecture.domain.dto.LectureCategoryByLectureDTO;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.entity.LectureCategoryByLecture;
import intbyte4.learnsmate.lecture_category_by_lecture.mapper.LectureCategoryByLectureMapper;
import intbyte4.learnsmate.lecture_category_by_lecture.repository.LectureCategoryByLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
