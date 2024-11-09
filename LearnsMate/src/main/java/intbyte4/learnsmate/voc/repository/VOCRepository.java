package intbyte4.learnsmate.voc.repository;

import intbyte4.learnsmate.voc.domain.VOC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VOCRepository extends JpaRepository<VOC, Long> {
}
