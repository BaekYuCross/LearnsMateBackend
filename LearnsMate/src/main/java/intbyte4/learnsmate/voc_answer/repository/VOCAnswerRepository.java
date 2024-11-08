package intbyte4.learnsmate.voc_answer.repository;

import intbyte4.learnsmate.voc_answer.domain.VOCAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VOCAnswerRepository extends JpaRepository<VOCAnswer, Long> {
}
