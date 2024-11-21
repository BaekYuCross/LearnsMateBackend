package intbyte4.learnsmate.lecture.repository;

import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.member.domain.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, String> , JpaSpecificationExecutor<Lecture> {
    List<Lecture> findAllByTutor(Member tutor);

    @Query("SELECT l FROM lecture l WHERE (:cursor IS NULL OR l.createdAt < :cursor) ORDER BY l.createdAt DESC")
    List<Lecture> findLecturesByCursor(@Param("cursor") LocalDateTime cursor, Pageable pageable);

    @Query("SELECT CONCAT(YEAR(l.createdAt), '-', LPAD(CAST(MONTH(l.createdAt) AS string), 2, '0')) AS monthYear, COUNT(l) " +
            "FROM lecture l " +
            "GROUP BY YEAR(l.createdAt), MONTH(l.createdAt) " +
            "ORDER BY YEAR(l.createdAt), MONTH(l.createdAt)")
    List<Object[]> findMonthlyLectureCounts();
}
