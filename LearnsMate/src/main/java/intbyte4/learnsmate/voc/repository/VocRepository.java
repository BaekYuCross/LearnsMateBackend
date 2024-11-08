package intbyte4.learnsmate.voc.repository;

import intbyte4.learnsmate.voc.domain.Voc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocRepository extends JpaRepository<Voc, Long> {
}
