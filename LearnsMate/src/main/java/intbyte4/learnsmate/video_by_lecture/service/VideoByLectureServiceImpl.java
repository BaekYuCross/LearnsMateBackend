package intbyte4.learnsmate.video_by_lecture.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.video_by_lecture.domain.dto.VideoByLectureDTO;
import intbyte4.learnsmate.video_by_lecture.domain.entity.VideoByLecture;
import intbyte4.learnsmate.video_by_lecture.mapper.VideoByLectureMapper;
import intbyte4.learnsmate.video_by_lecture.repository.VideoByLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoByLectureServiceImpl implements VideoByLectureService {

    private final VideoByLectureRepository videoByLectureRepository;
    private final LectureService lectureService;
    private final MemberService memberService;
    private final VideoByLectureMapper videoByLectureMapper;
    private final LectureMapper lectureMapper;
    private final MemberMapper memberMapper;


    // 강의코드별 모든 강의별 동영상 조회
    @Override
    public List<VideoByLectureDTO> findVideoByLectureByLectureCode(String lectureCode) {
        LectureDTO lectureDTO = lectureService.getLectureById(lectureCode);

        MemberDTO tutorDTO = memberService.findMemberByMemberCode(lectureDTO.getTutorCode(), MemberType.TUTOR);
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);

        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor);

        List<VideoByLecture> videoByLectures = videoByLectureRepository.findByLecture(lecture);
        if (videoByLectures.isEmpty()) {
            throw new CommonException(StatusEnum.VIDEO_BY_LECTURE_NOT_FOUND);
        }

        return videoByLectures.stream()
                .map(videoByLectureMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 강의별 동영상 등록
    @Override
    public void registerVideoByLecture(String lectureCode, VideoByLectureDTO videoByLectureDTO) {
        LectureDTO lectureDTO = lectureService.getLectureById(lectureCode);

        MemberDTO tutorDTO = memberService.findMemberByMemberCode(lectureDTO.getTutorCode(), MemberType.TUTOR);
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);

        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor);

        VideoByLecture videoByLecture = VideoByLecture.builder()
                .lecture(lecture)
                .videoTitle(videoByLectureDTO.getVideoTitle())
                .videoLink(videoByLectureDTO.getVideoLink())
                .build();

        VideoByLecture savedVideoByLecture = videoByLectureRepository.save(videoByLecture);

        videoByLectureMapper.toDTO(savedVideoByLecture);
    }

    // 동영상 제목과 링크 수정 메서드
    @Override
    public void updateVideoByLecture(VideoByLectureDTO videoByLectureDTO) {
        VideoByLecture videoByLecture = videoByLectureRepository.findById(videoByLectureDTO.getVideoCode())
                .orElseThrow(() -> new CommonException(StatusEnum.VIDEO_BY_LECTURE_NOT_FOUND));

        videoByLecture.toUpdate(videoByLectureDTO);

        VideoByLecture updatedVideoByLecture = videoByLectureRepository.save(videoByLecture);
        videoByLectureMapper.toDTO(updatedVideoByLecture);
    }
}
