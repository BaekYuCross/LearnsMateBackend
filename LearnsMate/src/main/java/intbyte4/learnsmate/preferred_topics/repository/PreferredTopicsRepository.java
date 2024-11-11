package intbyte4.learnsmate.preferred_topics.repository;

import intbyte4.learnsmate.preferred_topics.domain.entity.PreferredTopics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferredTopicsRepository extends JpaRepository<PreferredTopics, Long> {
}
