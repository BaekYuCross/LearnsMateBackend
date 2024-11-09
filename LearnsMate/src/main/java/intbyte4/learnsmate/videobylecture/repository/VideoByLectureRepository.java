package intbyte4.learnsmate.videobylecture.repository;

import intbyte4.learnsmate.videobylecture.domain.entity.VideoByLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoByLectureRepository extends JpaRepository<VideoByLecture, Long> {

    // Lecture의 lectureCode로 VideoByLecture의 개수를 카운트하는 쿼리
    @Query("SELECT COUNT(v) FROM video_by_lecture v WHERE v.lecture.lectureCode = :lectureCode")
    long countByLectureCode(@Param("lectureCode") Long lectureCode);
}
