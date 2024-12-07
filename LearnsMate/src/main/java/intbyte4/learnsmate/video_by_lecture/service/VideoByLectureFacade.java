package intbyte4.learnsmate.video_by_lecture.service;


import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.lecture_by_student.service.LectureByStudentService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.video_by_lecture.domain.dto.CountVideoByLectureDTO;
import intbyte4.learnsmate.video_by_lecture.repository.VideoByLectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoByLectureFacade {

    private final VideoByLectureRepository videoByLectureRepository;
    private final LectureService lectureService;
    private final MemberService memberService;
    private final LectureMapper lectureMapper;
    private final LectureByStudentService lectureByStudentService;
    private final MemberMapper memberMapper;


    // 강사별 강의와 강의별 동영상 개수 조회
    public List<CountVideoByLectureDTO> getVideoByLecture(Long tutorCode) {
        MemberDTO tutorDTO = memberService.findMemberByMemberCode(tutorCode, MemberType.TUTOR);
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);

        // 튜터와 관련된 강의 목록 조회
        List<LectureDTO> lectureDTOList = lectureService.getLecturesByTutorCode(tutor.getMemberCode());

        // 결과를 저장할 리스트 준비
        List<CountVideoByLectureDTO> result = new ArrayList<>();

        // 강의마다 처리
        for (LectureDTO lectureDTO : lectureDTOList) {

            long videoCount = videoByLectureRepository.countByLectureCode(lectureDTO.getLectureCode());

            long totalStudents = lectureByStudentService.countStudentsByLectureAndOwnStatus(lectureDTO.getLectureCode());

            result.add(CountVideoByLectureDTO.builder()
                    .lectureCode(lectureDTO.getLectureCode())
                    .totalStudents(totalStudents)
                    .lectureTitle(lectureDTO.getLectureTitle())
                    .videoCount(videoCount)
                    .build());
        }

        return result;
    }


}
