package intbyte4.learnsmate.comment.repository;

import intbyte4.learnsmate.comment.domain.entity.Comment;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByLecture(Lecture lecture);
}
