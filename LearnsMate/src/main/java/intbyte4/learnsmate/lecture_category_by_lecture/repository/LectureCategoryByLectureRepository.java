package intbyte4.learnsmate.lecture_category_by_lecture.repository;

import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureCategoryByLectureRepository extends JpaRepository<LectureCategory, Long> {
}
