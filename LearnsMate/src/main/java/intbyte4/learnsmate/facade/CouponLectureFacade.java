package intbyte4.learnsmate.facade;

import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.domain.vo.request.TutorCouponRegisterRequestVO;
import intbyte4.learnsmate.coupon.mapper.CouponMapper;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.coupon_by_lecture.service.CouponByLectureService;
import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.member.domain.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponLectureFacade {

    private final CouponService couponService;
    private final LectureService lectureService;
    private final CouponByLectureService couponByLectureService;
    private final CouponMapper couponMapper;
    private final LectureMapper lectureMapper;

    @Transactional
    public CouponDTO tutorRegisterCoupon(TutorCouponRegisterRequestVO request, Member tutor, CouponCategory couponCategory, Long lectureCode) {
        CouponEntity newCouponEntity = couponMapper.newCouponEntity(request, tutor, couponCategory);

        LectureDTO lectureDTO = lectureService.getLectureById(lectureCode);
        Lecture lectureEntity = lectureMapper.toEntity(lectureDTO, tutor);

        couponService.saveCoupon(newCouponEntity);
        couponByLectureService.registerCouponByLecture(lectureEntity, newCouponEntity);
        return couponMapper.toDTO(newCouponEntity);
    }
}

