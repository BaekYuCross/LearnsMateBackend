package intbyte4.learnsmate.video_by_lecture.service;

import intbyte4.learnsmate.video_by_lecture.domain.dto.VideoByLectureDTO;

import java.util.List;

public interface VideoByLectureService {
    // 강의코드별 모든 강의별 동영상 조회
    List<VideoByLectureDTO> findVideoByLectureByLectureCode(String lectureCode);

    void registerVideoByLecture(String lectureCode, VideoByLectureDTO videoByLectureDTO);

    // 동영상 제목과 링크 수정 메서드
    void updateVideoByLecture(VideoByLectureDTO videoByLectureDTO);
}
