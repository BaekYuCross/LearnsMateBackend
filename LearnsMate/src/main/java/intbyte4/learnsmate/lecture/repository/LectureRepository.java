package intbyte4.learnsmate.lecture.repository;


import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture,Long> , JpaSpecificationExecutor<Lecture> {
    List<Lecture> findAllByTutorCode(Long tutorCode);
}
