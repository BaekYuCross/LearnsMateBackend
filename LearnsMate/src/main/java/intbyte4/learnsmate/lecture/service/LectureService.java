package intbyte4.learnsmate.lecture.service;

import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.dto.MonthlyLectureCountDTO;

import java.util.List;

public interface LectureService {
    LectureDTO getLectureById(String lectureCode);

    void incrementClickCount(String lectureCode);

    List<LectureDTO> getLecturesByTutorCode(Long tutorCode);

    void updateLectureConfirmStatus(String lectureCode);

    List<MonthlyLectureCountDTO> getMonthlyLectureCounts();
}