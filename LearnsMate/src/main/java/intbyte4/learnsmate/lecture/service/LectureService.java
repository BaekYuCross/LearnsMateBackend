package intbyte4.learnsmate.lecture.service;

import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LectureService {
    List<LectureDTO> getAllLecture();
    LectureDTO getLectureById(Long lectureCode);
    List<LectureDTO> getLecturesByTutorCode(Long tutorCode);
    LectureDTO registerLecture(LectureDTO lectureDTO, List<Integer> lectureCategoryCodeList);
    LectureDTO updateLecture(LectureDTO lectureDTO);
    LectureDTO removeLecture(Long lectureCode);
}