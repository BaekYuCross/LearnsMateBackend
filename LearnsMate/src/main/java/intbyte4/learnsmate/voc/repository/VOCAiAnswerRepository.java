package intbyte4.learnsmate.voc.repository;

import intbyte4.learnsmate.voc.domain.VOCAiAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VOCAiAnswerRepository extends JpaRepository<VOCAiAnswer, Long> {
    List<VOCAiAnswer> findAllByAnalysisDate(LocalDate startOfWeek);

    boolean existsByAnalysisDateAndKeyword(LocalDate now, String keyword);
}

