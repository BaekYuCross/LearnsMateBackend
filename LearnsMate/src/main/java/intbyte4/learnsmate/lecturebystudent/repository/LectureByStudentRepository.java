package intbyte4.learnsmate.lecturebystudent.repository;

import intbyte4.learnsmate.lecturebystudent.domain.entity.LectureByStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureByStudentRepository extends JpaRepository<LectureByStudent, Long> {
}
