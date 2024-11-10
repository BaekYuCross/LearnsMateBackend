package intbyte4.learnsmate.videobylecture.service;

import intbyte4.learnsmate.videobylecture.domain.dto.CountVideoByLectureDTO;

public interface VideoByLectureService {
    // 강의의 동영상 개수 조회
    CountVideoByLectureDTO getVideoByLecture(Long lectureCode);
}
