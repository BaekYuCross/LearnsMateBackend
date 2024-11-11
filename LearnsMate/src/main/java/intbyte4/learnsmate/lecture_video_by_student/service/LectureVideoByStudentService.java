package intbyte4.learnsmate.lecture_video_by_student.service;

import intbyte4.learnsmate.lecture_video_by_student.domain.dto.LectureVideoProgressDTO;

import java.util.List;

public interface LectureVideoByStudentService {
    List<LectureVideoProgressDTO> getVideoProgressByStudent(Long studentCode);
}
