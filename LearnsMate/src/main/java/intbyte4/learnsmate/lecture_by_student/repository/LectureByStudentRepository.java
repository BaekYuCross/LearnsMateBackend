package intbyte4.learnsmate.lecture_by_student.repository;

import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture_by_student.domain.entity.LectureByStudent;
import intbyte4.learnsmate.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureByStudentRepository extends JpaRepository<LectureByStudent, Long> {

    List<LectureByStudent> findByStudentAndOwnStatus(Member student, boolean ownStatus);

    @Query("SELECT l.lectureByStudentCode FROM lecture_by_student l WHERE l.lecture.lectureCode = :lectureCode")
    List<Long> findLectureByStudentCodesByLectureCode(@Param("lectureCode") Long lectureCode);

    // 강의 코드와 ownStatus가 true인 수강생의 개수를 조회하는 쿼리
    @Query("SELECT COUNT(ls) FROM lecture_by_student ls " +
            "WHERE ls.lecture.lectureCode = :lectureCode AND ls.ownStatus = true")
    long countByLectureAndOwnStatus(@Param("lectureCode") Long lectureCode);

    // 강의 코드로 학생별 강의에 대한 결제 금액을 합산하는 쿼리
    @Query("SELECT SUM(p.paymentPrice) FROM payment p " +
            "JOIN p.lectureByStudent ls " +
            "WHERE ls.lecture.lectureCode = :lectureCode AND ls.ownStatus = true")
    int calculateTotalRevenueByLecture(@Param("lectureCode") Long lectureCode);

    LectureByStudent findByLecture(Lecture lecture);

    LectureByStudent findByLectureAndStudent(Lecture lecture, Member member);
}
