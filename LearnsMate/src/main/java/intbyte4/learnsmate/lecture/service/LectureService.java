package intbyte4.learnsmate.lecture.service;

import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.vo.request.RequestEditLectureInfoVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LectureService {
    List<LectureDTO> getAllLecture();
    LectureDTO getLectureById(Long lectureCode);
    LectureDTO registerLecture(Long lectureCode);
    LectureDTO updateLecture(Long lectureId, RequestEditLectureInfoVO requestEditLectureInfoVO);
    LectureDTO removeLecture(Long lectureCode);
}
