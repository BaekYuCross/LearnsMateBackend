package intbyte4.learnsmate.voc.repository;

import intbyte4.learnsmate.voc.domain.VOC;
import intbyte4.learnsmate.voc.domain.dto.VOCCategoryCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VOCRepository extends JpaRepository<VOC, String>, VOCRepositoryCustom {

    @Query("SELECT v FROM Voc v " +
            "WHERE v.member.memberCode = :memberCode " +
              "AND v.vocAnswerStatus = false")
    List<VOC> findUnansweredVOCByMember(@Param("memberCode") Long memberCode);

    @Query("SELECT v FROM Voc v " +
            "WHERE v.member.memberCode = :memberCode " +
            "AND v.vocAnswerStatus = true")
    List<VOC> findAnsweredVOCByMember(@Param("memberCode") Long memberCode);

    @Query("SELECT COUNT(v) FROM Voc v WHERE v.vocCategory.vocCategoryCode = :vocCategoryCode AND v.createdAt BETWEEN :startDate AND :endDate")
    long countByVocCategoryCodeAndDateRange(@Param("vocCategoryCode") Integer vocCategoryCode,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);

    long countByVocCategory_VocCategoryCode(Integer vocCategoryCode);

    @Query("SELECT new intbyte4.learnsmate.voc.domain.dto.VOCCategoryCountDTO(vc.vocCategoryCode, vc.vocCategoryName, COUNT(v)) " +
            "FROM Voc v " +
            "JOIN v.vocCategory vc  " +
            "GROUP BY vc.vocCategoryCode, vc.vocCategoryName")
    List<VOCCategoryCountDTO> countVocByCategory();

    @Query("SELECT new intbyte4.learnsmate.voc.domain.dto.VOCCategoryCountDTO(vc.vocCategoryCode, vc.vocCategoryName, COUNT(v)) " +
            "FROM Voc v " +
            "JOIN v.vocCategory vc " +
            "WHERE v.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY vc.vocCategoryCode, vc.vocCategoryName")
    List<VOCCategoryCountDTO> countVocByCategoryWithinDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(nativeQuery = true, value = """
    SELECT keyword, COUNT(*) AS keywordCount
    FROM (
        SELECT 
            TRIM(REGEXP_REPLACE(v.voc_content, '([가-힣]+)(을|를|이|가|과|와|에서|까지)$', '\\1')) AS keyword
        FROM voc v
        WHERE v.created_at BETWEEN :startDate AND :endDate
    ) AS processed
    WHERE keyword != ''
    GROUP BY keyword
    ORDER BY keywordCount DESC
    """)
    List<Object[]> findKeywordFrequencyBetweenDates(@Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);

    @Query("""
        SELECT v
        FROM Voc v
        JOIN VocAnswer va
          ON v.vocCode = va.voc.vocCode
        WHERE v.createdAt BETWEEN :startDate AND :endDate
          AND v.vocContent LIKE %:keyword%
          AND (v.vocAnswerSatisfaction = '보통' OR v.vocAnswerSatisfaction = '만족')
    """)
    List<VOC> findVocByKeywordAndSatisfaction(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate,
                                              @Param("keyword") String keyword);

    // 현재 시각보다 이전의 created_at을 가져오기
    @Query("SELECT v FROM Voc v WHERE v.createdAt <= :now")
    Page<VOC> findAllBeforeNow(@Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT v FROM Voc v WHERE v.vocAnswerStatus = false ORDER BY v.createdAt DESC")
    List<VOC> findTop3ByVocAnswerStatusFalseOrderByCreatedAtDesc();

}
