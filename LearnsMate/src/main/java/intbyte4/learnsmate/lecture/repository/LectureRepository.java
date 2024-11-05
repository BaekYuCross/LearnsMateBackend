package intbyte4.learnsmate.lecture.repository;


import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<Lecture,Long> {
}
