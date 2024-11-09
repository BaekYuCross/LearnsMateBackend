package intbyte4.learnsmate.lecturebystudent.service;

import intbyte4.learnsmate.lecturebystudent.domain.dto.LectureByStudentDTO;

import java.util.List;

public interface LectureByStudentService {
    // 학생별 모든 강의 조회
    List<LectureByStudentDTO> findByStudentCode(Long studentCode);
}
