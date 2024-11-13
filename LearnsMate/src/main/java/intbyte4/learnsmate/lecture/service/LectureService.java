package intbyte4.learnsmate.lecture.service;

import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface LectureService {
    List<LectureDTO> getAllLecture();
    LectureDTO getLectureById(Long lectureCode);
    List<LectureDTO> getLecturesByTutorCode(Long tutorCode);
    LectureDTO updateLecture(LectureDTO lectureDTO);

    // 강의별 계약과정이 강의 코드가 7개 라면 강의컬럼의 승인여부 true로 변환
    LectureDTO updateLectureConfirmStatus(Long lectureCode);
}