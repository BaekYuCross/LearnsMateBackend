package intbyte4.learnsmate.lecture.service;

import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.dto.LectureFilterDTO;
import intbyte4.learnsmate.lecture.domain.dto.MonthlyLectureCountDTO;
import intbyte4.learnsmate.lecture.domain.dto.MonthlyLectureFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LectureService {
    LectureDTO getLectureById(String lectureCode);

    void incrementClickCount(String lectureCode);

    List<LectureDTO> getLecturesByTutorCode(Long tutorCode);

    void updateLectureConfirmStatus(String lectureCode);

    List<MonthlyLectureCountDTO> getMonthlyLectureCounts();

    Page<LectureDTO> filterLectureWithPaging(LectureFilterDTO filterDTO, Pageable pageable);

    List<MonthlyLectureCountDTO> getFilteredMonthlyLectureCounts(MonthlyLectureFilterDTO filterDTO);
}