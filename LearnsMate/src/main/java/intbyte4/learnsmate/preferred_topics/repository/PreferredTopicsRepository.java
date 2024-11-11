package intbyte4.learnsmate.preferred_topics.repository;

import intbyte4.learnsmate.preferred_topics.domain.entity.PreferredTopics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferredTopicsRepository extends JpaRepository<PreferredTopics, Long> {

    List<PreferredTopics> findByMember_MemberCode(Long memberCode);
}
