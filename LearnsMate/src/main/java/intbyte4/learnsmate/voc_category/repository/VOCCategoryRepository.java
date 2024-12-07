package intbyte4.learnsmate.voc_category.repository;

import intbyte4.learnsmate.voc_category.domain.VOCCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VOCCategoryRepository extends JpaRepository<VOCCategory, Integer> {
}
