package intbyte4.learnsmate.lecturevideobystudent.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.lecturebystudent.domain.dto.LectureByStudentDTO;
import intbyte4.learnsmate.lecturebystudent.service.LectureByStudentService;
import intbyte4.learnsmate.lecturevideobystudent.domain.dto.LectureVideoProgressDTO;
import intbyte4.learnsmate.lecturevideobystudent.repository.LectureVideoByStudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureVideoByStudentServiceImpl implements LectureVideoByStudentService {

    private final LectureVideoByStudentRepository lectureVideoByStudentRepository;
    private final LectureByStudentService lectureByStudentService;

    @Override
    public List<LectureVideoProgressDTO> getVideoProgressByStudent(Long studentCode) {
        // 학생이 수강한 모든 강의 목록 조회
        List<LectureByStudentDTO> lecturesByStudent = lectureByStudentService.findByStudentCode(studentCode);
        if (lecturesByStudent.isEmpty()) {
            throw new CommonException(StatusEnum.LECTURE_NOT_FOUND);
        }

        List<LectureVideoProgressDTO> progressList = new ArrayList<>();

        for (LectureByStudentDTO lectureByStudentDTO : lecturesByStudent) {
            // 각 강의의 강의 코드
            Long lectureCode = lectureByStudentDTO.getLectureByStudentCode();

            // 강의에 속한 동영상 총 개수 조회
            long totalVideos = lectureVideoByStudentRepository.countByLectureCode(lectureCode);
            String lectureTitle = lectureByStudentDTO.getLecture().getLectureTitle();

            // 완료된 동영상 수 조회 (lectureStatus가 true인 것만 카운트)
            long completedVideos = lectureVideoByStudentRepository.countCompletedVideos(lectureCode);

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
