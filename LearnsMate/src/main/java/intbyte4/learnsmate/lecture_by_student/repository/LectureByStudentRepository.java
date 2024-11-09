package intbyte4.learnsmate.lecture_by_student.repository;

import intbyte4.learnsmate.lecture_by_student.domain.entity.LectureByStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureByStudentRepository extends JpaRepository<LectureByStudent, Long> {

//    List<LectureByStudent> findByStudentCode(Long memberCode);
}
