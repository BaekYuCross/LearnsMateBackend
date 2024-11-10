package intbyte4.learnsmate.lecturevideobystudent.service;

import intbyte4.learnsmate.lecturevideobystudent.domain.dto.LectureVideoProgressDTO;

import java.util.List;

public interface LectureVideoByStudentService {
    List<LectureVideoProgressDTO> getVideoProgressByStudent(Long studentCode);
}
