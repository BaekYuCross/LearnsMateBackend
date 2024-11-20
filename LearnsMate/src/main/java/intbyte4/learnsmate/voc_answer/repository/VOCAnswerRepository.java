package intbyte4.learnsmate.voc_answer.repository;

import intbyte4.learnsmate.voc_answer.domain.VOCAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VOCAnswerRepository extends JpaRepository<VOCAnswer, Long> {
    Optional<VOCAnswer> findByVoc_VocCode(String vocCode);
}
