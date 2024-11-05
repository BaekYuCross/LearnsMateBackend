package intbyte4.learnsmate.lecture.service;


import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.domain.specification.LectureSpecifications;
import intbyte4.learnsmate.lecture.domain.vo.EditLectureInfoVO;
import intbyte4.learnsmate.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {
    
    private final LectureRepository lectureRepository;

    // 전체 강의 조회
    public List<LectureDTO> getAllLecture() {
        List<Lecture> lectureList = lectureRepository.findAll();
        if (lectureList.isEmpty()) throw new CommonException(StatusEnum.LECTURE_NOT_FOUND);
        return lectureList.stream()
                .map(Lecture::convertToDTO)
                .collect(Collectors.toList());
    }

    // 강의 단건 조회
    public LectureDTO getLectureById(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_NOT_FOUND));
        return lecture.convertToDTO();
    }


    // 카테고리별 강의 조회
//    public List<Lecture> filterLectures(LectureFilterDTO filter) {
//        // vo로 사용해야할까 어떻게 해야할까
//        Specification<Lecture> spec = LectureSpecifications.filterByCriteria(filter);
//        return lectureRepository.findAll(spec);
//    }

    // 강의 등록
//    @Transactional
//    public LectureDTO createLecture(LectureDTO lectureDTO) {
//        Lecture lecture = lectureDTO.toLecture();
//        Lecture savedLecture = lectureRepository.save(lecture);
//        return savedLecture.convertToDTO();
//    }

    // 강의 수정
    @Transactional
    public LectureDTO updateLecture(Long lectureId, EditLectureInfoVO editLectureInfoVO) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_NOT_FOUND));
        lecture.toUpdate(editLectureInfoVO);
        lectureRepository.save(lecture);
        return lecture.convertToDTO();
    }

}
