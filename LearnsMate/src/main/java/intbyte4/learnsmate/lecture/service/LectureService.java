package intbyte4.learnsmate.lecture.service;

import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.dto.TutorLectureVideoCountDTO;
import intbyte4.learnsmate.lecture.enums.LectureCategoryEnum;
import intbyte4.learnsmate.lecture.enums.LectureLevelEnum;
import intbyte4.learnsmate.member.domain.entity.Member;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LectureService {
    List<LectureDTO> getAllLecture();
    LectureDTO getLectureById(Long lectureCode);
    List<LectureDTO> getLecturesByTutorCode(Long tutorCode);
    LectureDTO registerLecture(LectureDTO lectureDTO);
    LectureDTO updateLecture(Long lectureId, LectureDTO requestEditLectureInfoVO);
    LectureDTO removeLecture(Long lectureCode);
    List<TutorLectureVideoCountDTO> getVideoCountByLecture(Long tutorCode);
}
