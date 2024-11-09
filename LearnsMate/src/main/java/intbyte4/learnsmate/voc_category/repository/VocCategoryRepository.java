package intbyte4.learnsmate.voc_category.repository;

import intbyte4.learnsmate.voc_category.domain.VocCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocCategoryRepository extends JpaRepository<VocCategory, Integer> {
}
