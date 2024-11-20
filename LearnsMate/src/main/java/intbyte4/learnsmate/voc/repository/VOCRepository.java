package intbyte4.learnsmate.voc.repository;

import intbyte4.learnsmate.voc.domain.VOC;
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
}
