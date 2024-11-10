package intbyte4.learnsmate.lecture_by_student.repository;

import intbyte4.learnsmate.lecture_by_student.domain.entity.LectureByStudent;
import intbyte4.learnsmate.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureByStudentRepository extends JpaRepository<LectureByStudent, Long> {

    List<LectureByStudent> findByStudentAndRefundStatus(Member student, boolean refundStatus);
}
