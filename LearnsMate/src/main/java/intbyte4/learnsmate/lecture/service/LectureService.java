package intbyte4.learnsmate.lecture.service;

import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;


import java.util.List;

public interface LectureService {
    List<LectureDTO> getAllLecture();
    LectureDTO getLectureById(Long lectureCode);

    // 강사별 강의 모두 조회
    List<LectureDTO> getLecturesByTutorCode(Long tutorCode);

    LectureDTO updateLecture(LectureDTO lectureDTO);
}