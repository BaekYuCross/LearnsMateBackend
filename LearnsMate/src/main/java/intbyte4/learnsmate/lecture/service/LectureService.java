package intbyte4.learnsmate.lecture.service;

import intbyte4.learnsmate.lecture.domain.vo.response.ResponseFindLectureVO;

import java.util.List;

public interface LectureService {
    // 전체 강의 조회
    List<ResponseFindLectureVO> getAllLecture();
}
