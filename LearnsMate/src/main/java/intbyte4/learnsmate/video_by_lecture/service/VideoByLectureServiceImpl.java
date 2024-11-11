package intbyte4.learnsmate.video_by_lecture.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.lecture_category.domain.dto.LectureCategoryDTO;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategory;
import intbyte4.learnsmate.lecture_category.mapper.LectureCategoryMapper;
import intbyte4.learnsmate.lecture_category.service.LectureCategoryService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.video_by_lecture.domain.dto.CountVideoByLectureDTO;
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
    private final LectureCategoryService lectureCategoryService;
    private final VideoByLectureMapper videoByLectureMapper;
    private final LectureMapper lectureMapper;
    private final MemberMapper memberMapper;
    private final LectureCategoryMapper lectureCategoryMapper;

    // 강사별 강의와 강의별 동영상 개수 조회
    @Override
    public CountVideoByLectureDTO getVideoByLecture(Long lectureCode) {

        LectureDTO lectureDTO = lectureService.getLectureById(lectureCode);

        MemberDTO tutorDTO = memberService.findMemberByMemberCode(lectureDTO.getTutorCode(), MemberType.TUTOR);
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);

//        LectureCategoryDTO lectureCategoryDTO = lectureCategoryService.findByLectureCategoryCode(lectureDTO.getLectureCategoryCode());
//        LectureCategory lectureCategory = lectureCategoryMapper.toEntity(lectureCategoryDTO);

//        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor, lectureCategory);
        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor);

        long videoCount = videoByLectureRepository.countByLectureCode(lecture);

        return CountVideoByLectureDTO.builder()
                .lectureCode(lectureCode)
                .lectureTitle(lectureDTO.getLectureTitle())
                .videoCount(videoCount)
                .build();
    }

    // 강의코드별 모든 강의별 동영상 조회
    @Override
    public List<VideoByLectureDTO> findVideoByLectureByLectureCode(Long lectureCode) {
        LectureDTO lectureDTO = lectureService.getLectureById(lectureCode);

        MemberDTO tutorDTO = memberService.findMemberByMemberCode(lectureDTO.getTutorCode(), MemberType.TUTOR);
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);

//        LectureCategoryDTO lectureCategoryDTO = lectureCategoryService.findByLectureCategoryCode(lectureDTO.getLectureCategoryCode());
//        LectureCategory lectureCategory = lectureCategoryMapper.toEntity(lectureCategoryDTO);

//        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor, lectureCategory);
        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor);

        List<VideoByLecture> videoByLectures = videoByLectureRepository.findByLecture(lecture);
        if (videoByLectures.isEmpty()) {
            throw new CommonException(StatusEnum.VIDEO_BY_LECTURE_NOT_FOUND);
        }

        return videoByLectures.stream()
                .map(videoByLectureMapper::toDTO)
                .collect(Collectors.toList());
}

}
