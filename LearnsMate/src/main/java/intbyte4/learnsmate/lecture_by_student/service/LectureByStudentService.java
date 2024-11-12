package intbyte4.learnsmate.lecture_by_student.service;

import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture_by_student.domain.dto.LectureByStudentDTO;
import jakarta.transaction.Transactional;
import intbyte4.learnsmate.member.domain.entity.Member;

import java.util.List;

public interface LectureByStudentService {
    // 학생별 모든 강의 조회
    List<LectureByStudentDTO> findByStudentCode(Long studentCode);

//    // 강의별 학생코드 개수 조회 refund_status 가 true인것만
//    long countStudentsByLectureAndOwnStatus(Long lectureCode);

//    @Transactional
//    void updateOwnStatus(Lecture lecture);

    void registerLectureByStudent(LectureByStudentDTO lectureByStudentDTO, Lecture lecture, Member member);

    Long findStudentCodeByLectureCode(Lecture lecture);

    LectureByStudentDTO findByLectureAndStudent(Lecture lecture, Member member);
}
