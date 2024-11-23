package intbyte4.learnsmate.lecture_category_by_lecture.repository;

import intbyte4.learnsmate.lecture_category.domain.dto.LectureCategoryDTO;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.dto.LectureCategoryByLectureDTO;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.entity.LectureCategoryByLecture;
import intbyte4.learnsmate.member.domain.dto.CategoryCountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LectureCategoryByLectureRepository extends JpaRepository<LectureCategoryByLecture, Long> {
    @Query("SELECT lc.lectureCategoryName " +
            "FROM lectureCategoryByLecture lcl " +
            "JOIN lcl.lectureCategory lc " +
            "WHERE lcl.lecture.lectureCode = :lectureCode")
    List<String> findCategoryNamesByLectureCode(@Param("lectureCode") String lectureCode);

    @Modifying
    @Query("DELETE FROM lectureCategoryByLecture lcl " +
            "WHERE lcl.lecture.lectureCode = :lectureCode")
    void deleteAllByLectureCode(@Param("lectureCode") String lectureCode);

    @Query("SELECT new intbyte4.learnsmate.lecture_category_by_lecture.domain.dto.LectureCategoryByLectureDTO(" +
            "lcl.lectureCategoryByLectureCode, lcl.lecture.lectureCode, lcl.lectureCategory.lectureCategoryCode) " +
            "FROM lectureCategoryByLecture lcl " +
            "WHERE lcl.lecture.lectureCode = :lectureCode")
    LectureCategoryByLectureDTO findLectureCategoryDetailsByLectureCode(@Param("lectureCode") String lectureCode);

    @Query("SELECT lc.lectureCategoryCode, COUNT(lcb) " +
            "FROM lectureCategoryByLecture lcb " +
            "JOIN lcb.lectureCategory lc " +
            "GROUP BY lc.lectureCategoryCode")
    List<CategoryCountDTO> countLecturesByCategory();

    @Query("SELECT new intbyte4.learnsmate.member.domain.dto.CategoryCountDTO(" +
            "lc.lectureCategory.lectureCategoryCode, lc.lectureCategory.lectureCategoryName, COUNT(p)) " +
            "FROM lectureCategoryByLecture lc " +
            "JOIN lc.lecture l " +
            "JOIN lecture_by_student lbs ON l.lectureCode = lbs.lecture.lectureCode " +
            "JOIN payment p ON p.lectureByStudent = lbs " +
            "WHERE p.createdAt BETWEEN :startDate AND :endDate " +
            "AND lbs.ownStatus = true " +
            "GROUP BY lc.lectureCategory.lectureCategoryCode")
    List<CategoryCountDTO> countLecturesByCategoryWithinDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    LectureCategoryByLecture findByLecture_LectureCode(String lectureCode);
}