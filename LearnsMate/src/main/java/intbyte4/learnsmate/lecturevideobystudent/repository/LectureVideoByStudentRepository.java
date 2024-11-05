package intbyte4.learnsmate.lecturevideobystudent.repository;

import intbyte4.learnsmate.lecturevideobystudent.domain.entity.LectureVideoByStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureVideoByStudentRepository extends JpaRepository<LectureVideoByStudent, Long> {
}
