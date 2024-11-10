package intbyte4.learnsmate.video_by_lecture.service;

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
import intbyte4.learnsmate.video_by_lecture.repository.VideoByLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoByLectureServiceImpl implements VideoByLectureService {

    private final VideoByLectureRepository videoByLectureRepository;
    private final LectureService lectureService;
    private final MemberService memberService;
    private final LectureCategoryService lectureCategoryService;
    private final LectureMapper lectureMapper;
    private final MemberMapper memberMapper;
    private final LectureCategoryMapper lectureCategoryMapper;

    // 강의의 동영상 개수 조회
    @Override
    public CountVideoByLectureDTO getVideoByLecture(Long lectureCode) {

        LectureDTO lectureDTO = lectureService.getLectureById(lectureCode);

        MemberDTO tutorDTO = memberService.findMemberByMemberCode(lectureDTO.getTutorCode(), MemberType.TUTOR);
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);

        LectureCategoryDTO lectureCategoryDTO = lectureCategoryService.findByLectureCategoryCode(lectureDTO.getLectureCategoryCode());
        LectureCategory lectureCategory = lectureCategoryMapper.toEntity(lectureCategoryDTO);

        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor, lectureCategory);
        long videoCount = videoByLectureRepository.countByLectureCode(lecture);
        return new CountVideoByLectureDTO(lectureCode, videoCount);
    }

}
