package intbyte4.learnsmate.lecture_video_by_student.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.lecture_by_student.domain.dto.LectureByStudentDTO;
import intbyte4.learnsmate.lecture_by_student.service.LectureByStudentService;
import intbyte4.learnsmate.lecture_video_by_student.domain.dto.LectureVideoProgressDTO;
import intbyte4.learnsmate.lecture_video_by_student.repository.LectureVideoByStudentRepository;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureVideoByStudentServiceImpl implements LectureVideoByStudentService {

    private final LectureVideoByStudentRepository lectureVideoByStudentRepository;
    private final LectureByStudentService lectureByStudentService;
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    // 강의코드 , 강의 제목 , 총 동영상 수 , 완료된 동영상 수 , 진척도 조회
    @Override
    public List<LectureVideoProgressDTO> getVideoProgressByStudent(Long studentCode) {
        // 학생 조회
        MemberDTO studentDTO = memberService.findMemberByMemberCode(studentCode, MemberType.STUDENT);
        Member student = memberMapper.fromMemberDTOtoMember(studentDTO);

        // 학생이 수강한 모든 강의 목록 조회
        List<LectureByStudentDTO> lecturesByStudent = lectureByStudentService.findByStudentCode(student.getMemberCode());

        List<LectureVideoProgressDTO> progressList = new ArrayList<>();

        for (LectureByStudentDTO lectureByStudentDTO : lecturesByStudent) {
            // 각 강의의 강의 코드
            String lectureCode = lectureByStudentDTO.getLecture().getLectureCode();

            // 강의에 속한 동영상 총 개수 조회
            int totalVideos = lectureVideoByStudentRepository.countByLectureCode(lectureCode);
            String lectureTitle = lectureByStudentDTO.getLecture().getLectureTitle();

            // 완료된 동영상 수 조회 (lectureStatus가 true인 것만 카운트)
            int completedVideos = lectureVideoByStudentRepository.countCompletedVideos(lectureCode);

            // 진척도 계산 (완료된 동영상 수 / 총 동영상 수)
            double progress = totalVideos > 0 ? (double) completedVideos / totalVideos * 100 : 0;

            // DTO 생성하여 리스트에 추가
            progressList.add(LectureVideoProgressDTO.builder()
                    .lectureCode(lectureCode)
                    .lectureTitle(lectureTitle)
                    .totalVideos(totalVideos)
                    .completedVideos(completedVideos)
                    .progress(progress)
                    .build());
        }

        return progressList;
    }

}
