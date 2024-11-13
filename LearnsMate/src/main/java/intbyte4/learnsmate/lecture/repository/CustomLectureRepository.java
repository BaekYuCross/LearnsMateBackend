package intbyte4.learnsmate.lecture.repository;

import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.domain.vo.response.ResponseLectureFilterRequestVO;

import java.util.List;

public interface CustomLectureRepository {
    List<Lecture> findLecturesByFilters(ResponseLectureFilterRequestVO request);
}
