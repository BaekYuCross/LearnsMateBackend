package intbyte4.learnsmate.lecture.service;

import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LectureService {
    List<LectureDTO> getAllLecture();
    LectureDTO getLectureById(Long lectureCode);
    List<LectureDTO> getLecturesByTutorCode(Long tutorCode);
    LectureDTO registerLecture(LectureDTO lectureDTO, List<Integer> lectureCategoryCodeList);
    LectureDTO updateLecture(Long lectureId, LectureDTO requestEditLectureInfoVO);
    LectureDTO removeLecture(Long lectureCode);
    LectureDTO discountLecture(LectureDTO lectureCode, IssueCouponDTO couponCode);
}