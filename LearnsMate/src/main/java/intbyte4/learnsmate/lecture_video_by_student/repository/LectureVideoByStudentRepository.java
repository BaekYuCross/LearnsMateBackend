package intbyte4.learnsmate.lecture_video_by_student.repository;

import intbyte4.learnsmate.lecture_video_by_student.domain.entity.LectureVideoByStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureVideoByStudentRepository extends JpaRepository<LectureVideoByStudent, Long> {

    // 강의에 속한 동영상의 총 개수 카운트
    @Query("SELECT COUNT(lvs) FROM LectureVideoByStudent lvs WHERE lvs.videoCode.lecture.lectureCode = :lectureCode")
    long countByLectureCode(Long lectureCode);

    // 강의에 속한 완료된 동영상만 카운트 (lectureStatus가 true인 경우)
    @Query("SELECT COUNT(lvs) FROM LectureVideoByStudent lvs WHERE lvs.videoCode.lecture.lectureCode = :lectureCode AND lvs.lectureStatus = true")
    long countCompletedVideos(Long lectureCode);
}

