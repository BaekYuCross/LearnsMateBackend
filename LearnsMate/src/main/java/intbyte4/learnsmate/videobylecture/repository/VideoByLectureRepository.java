package intbyte4.learnsmate.videobylecture.repository;

import intbyte4.learnsmate.videobylecture.domain.entity.VideoByLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoByLectureRepository extends JpaRepository<VideoByLecture, Long> {
}
