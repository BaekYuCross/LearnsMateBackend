package intbyte4.learnsmate.lecture.service;

import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;

import java.util.List;

public interface LectureService {
    List<LectureDTO> getAllLecture();
    LectureDTO getLectureById(Long lectureCode);
    LectureDTO registerLecture(LectureDTO lectureDTO, List<Integer> lectureCategoryCodeList);
    LectureDTO updateLecture(Long lectureId, LectureDTO requestEditLectureInfoVO);
}