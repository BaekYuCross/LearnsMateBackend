package intbyte4.learnsmate.preferred_topics.repository;

import intbyte4.learnsmate.preferred_topics.domain.dto.PreferredTopicStatsDTO;
import intbyte4.learnsmate.preferred_topics.domain.entity.PreferredTopics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PreferredTopicsRepository extends JpaRepository<PreferredTopics, Long> {

    List<PreferredTopics> findByMember_MemberCode(Long memberCode);

    // 입력값 2개에 따라 월별 카테고리별 선호주제 개수 구하는 코드 (2023년도 1월의 7개 강의 카테고리(선호주제)의 비율)
    @Query("SELECT new intbyte4.learnsmate.preferred_topics.domain.dto.PreferredTopicStatsDTO(" +
            "YEAR(m.createdAt), MONTH(m.createdAt), lc.lectureCategoryName, COUNT(pt.preferredTopicCode)) " +
            "FROM member m " +
            "JOIN preferredTopics pt ON m.memberCode = pt.member.memberCode " +
            "JOIN lecture_category lc ON pt.lectureCategory.lectureCategoryCode = lc.lectureCategoryCode " +
            "WHERE m.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY YEAR(m.createdAt), MONTH(m.createdAt), lc.lectureCategoryName " +
            "ORDER BY YEAR(m.createdAt), MONTH(m.createdAt), COUNT(pt.preferredTopicCode) DESC")
    List<PreferredTopicStatsDTO> findCategoryStatsByMonth(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // 특정 학생과 동일한 lectureCategory를 선호하는 다른 학생들의 memberCode를 조회
    @Query("SELECT pt.member " +
            "FROM preferredTopics pt " +
            "WHERE pt.lectureCategory IN (SELECT p.lectureCategory " +
            "FROM preferredTopics p " +
            "WHERE p.member = :studentCode) " +
            "AND pt.member != :studentCode")
    List<Long> findStudentsWithSimilarPreferredTopics(@Param("studentCode") Long studentCode);
}
