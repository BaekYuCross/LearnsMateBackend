package intbyte4.learnsmate.lecture.repository;

import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategoryEnum;
import intbyte4.learnsmate.member.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, String> , JpaSpecificationExecutor<Lecture>, CustomLectureRepository {
    List<Lecture> findAllByTutor(Member tutor);

    @Query("SELECT l FROM lecture l ORDER BY l.createdAt DESC")
    Page<Lecture> findLecturesByOffset(Pageable pageable);

    @Query("SELECT CONCAT(YEAR(l.createdAt), '-', LPAD(CAST(MONTH(l.createdAt) AS string), 2, '0')) AS monthYear, COUNT(l) " +
            "FROM lecture l " +
            "GROUP BY YEAR(l.createdAt), MONTH(l.createdAt) " +
            "ORDER BY YEAR(l.createdAt), MONTH(l.createdAt)")
    List<Object[]> findMonthlyLectureCounts();

    @Query("SELECT CONCAT(YEAR(l.createdAt), '-', LPAD(CAST(MONTH(l.createdAt) AS string), 2, '0')) AS monthYear, COUNT(l) " +
            "FROM lecture l " +
            "WHERE (YEAR(l.createdAt) > :startYear OR (YEAR(l.createdAt) = :startYear AND MONTH(l.createdAt) >= :startMonth)) " +
            "AND (YEAR(l.createdAt) < :endYear OR (YEAR(l.createdAt) = :endYear AND MONTH(l.createdAt) <= :endMonth)) " +
            "GROUP BY YEAR(l.createdAt), MONTH(l.createdAt) " +
            "ORDER BY YEAR(l.createdAt), MONTH(l.createdAt)")
    List<Object[]> findFilteredMonthlyLectureCounts(@Param("startYear") Integer startYear, @Param("startMonth") Integer startMonth, @Param("endYear") Integer endYear, @Param("endMonth") Integer endMonth);

    @Query("SELECT COALESCE(SUM(l.lectureClickCount), 0) FROM lecture l " +
            "WHERE l.createdAt BETWEEN :startDate AND :endDate")
    Integer sumClickCountBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(l.lectureClickCount, 0) FROM lecture l " +
            "WHERE l.lectureCode = :lectureCode " +
            "AND l.createdAt BETWEEN :startDate AND :endDate")
    Integer getClickCountByLectureCodeBetweenDates(@Param("lectureCode") String lectureCode, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(l.lectureClickCount), 0) FROM lecture l")
    int findTotalClickCount();

    @Query("SELECT COALESCE(SUM(l.lectureClickCount), 0) FROM lecture l " +
            "JOIN lectureCategoryByLecture lc " +
            "ON lc.lecture.lectureCode = l.lectureCode " +
            "WHERE lc.lectureCategory.lectureCategoryName = :categoryName")
    int findClickCountByCategory(@Param("categoryName") LectureCategoryEnum categoryName);

    @Query("SELECT COALESCE(SUM(l.lectureClickCount), 0) " +
            "FROM lecture l " +
            "JOIN lectureCategoryByLecture lc ON lc.lecture.lectureCode = l.lectureCode " +
            "WHERE lc.lectureCategory.lectureCategoryName = :categoryName " +
            "AND l.createdAt BETWEEN :startDate AND :endDate")
    int findClickCountByCategoryWithDateRange(@Param("categoryName") LectureCategoryEnum categoryName, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
