package intbyte4.learnsmate.video_by_lecture.service;

import intbyte4.learnsmate.video_by_lecture.domain.dto.CountVideoByLectureDTO;
import intbyte4.learnsmate.video_by_lecture.domain.dto.VideoByLectureDTO;

import java.util.List;

public interface VideoByLectureService {
    // 강의의 동영상 개수 조회
    CountVideoByLectureDTO getVideoByLecture(Long lectureCode);

    // 강의코드별 모든 강의별 동영상 조회
    List<VideoByLectureDTO> findVideoByLectureByLectureCode(Long lectureCode);
}
