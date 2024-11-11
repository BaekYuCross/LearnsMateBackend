package intbyte4.learnsmate.video_by_lecture.mapper;

import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.video_by_lecture.domain.dto.VideoByLectureDTO;
import intbyte4.learnsmate.video_by_lecture.domain.entity.VideoByLecture;
import intbyte4.learnsmate.video_by_lecture.domain.vo.response.ResponseFindVideoByLectureVO;
import org.springframework.stereotype.Component;

@Component
public class VideoByLectureMapper {


    // Entity -> DTO 변환
    public VideoByLectureDTO toDTO(VideoByLecture videoByLecture) {
        return VideoByLectureDTO.builder()
                .videoCode(videoByLecture.getVideoCode())
                .videoLink(videoByLecture.getVideoLink())
                .videoTitle(videoByLecture.getVideoTitle())
                .lectureCode(videoByLecture.getLecture().getLectureCode())
                .build();
    }

    // DTO -> Entity 변환
    public VideoByLecture toEntity(VideoByLectureDTO videoByLectureDTO, Lecture lecture) {
        return VideoByLecture.builder()
                .videoCode(videoByLectureDTO.getVideoCode())
                .videoLink(videoByLectureDTO.getVideoLink())
                .videoTitle(videoByLectureDTO.getVideoTitle())
                .lecture(lecture)
                .build();
    }

    // DTO -> Response VO 변환
    public ResponseFindVideoByLectureVO toResponseVO(VideoByLectureDTO videoByLectureDTO) {
        return ResponseFindVideoByLectureVO.builder()
                .videoCode(videoByLectureDTO.getVideoCode())
                .videoLink(videoByLectureDTO.getVideoLink())
                .videoTitle(videoByLectureDTO.getVideoTitle())
                .lectureCode(videoByLectureDTO.getLectureCode())
                .build();
    }

}
