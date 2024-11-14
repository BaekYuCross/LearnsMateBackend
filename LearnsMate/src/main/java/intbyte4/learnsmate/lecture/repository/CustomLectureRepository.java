package intbyte4.learnsmate.lecture.repository;

import intbyte4.learnsmate.lecture.domain.dto.LectureFilterDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.domain.vo.request.RequestLectureFilterVO;

import java.util.List;

public interface CustomLectureRepository {
    List<LectureFilterDTO> findLecturesByFilters(RequestLectureFilterVO request);
}
