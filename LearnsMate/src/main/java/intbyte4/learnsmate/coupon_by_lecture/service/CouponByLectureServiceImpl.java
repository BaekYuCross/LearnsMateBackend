package intbyte4.learnsmate.coupon_by_lecture.service;

import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon_by_lecture.domain.CouponByLecture;
import intbyte4.learnsmate.coupon_by_lecture.domain.dto.CouponByLectureDTO;
import intbyte4.learnsmate.coupon_by_lecture.mapper.CouponByLectureMapper;
import intbyte4.learnsmate.coupon_by_lecture.repository.CouponByLectureRepository;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("couponByLectureService")
@RequiredArgsConstructor
public class CouponByLectureServiceImpl implements CouponByLectureService {

    private final CouponByLectureRepository couponByLectureRepository;
    private final CouponByLectureMapper couponByLectureMapper;

    @Override
    public void registerCouponByLecture(Lecture lecture, CouponEntity couponEntity) {
        CouponByLecture couponByLecture = CouponByLecture.builder()
                .coupon(couponEntity)
                .lecture(lecture)
                .build();

        couponByLectureRepository.save(couponByLecture);
        log.info("CouponByLecture 등록 완료: {}", couponByLecture);
    }

    @Override
    public void updateCouponByLecture(CouponEntity coupon) {
        CouponByLecture relatedCoupon = couponByLectureRepository.findByCoupon(coupon);
        relatedCoupon.updateCouponDetails(coupon);
        couponByLectureRepository.save(relatedCoupon);
        log.info("CouponByLecture 수정 완료: {}", relatedCoupon);
    }

    @Override
    public List<CouponByLectureDTO> findCouponByLecture(Long tutorCode) {
        List<CouponByLecture> couponByLectureList = couponByLectureRepository.findByTutorCode(tutorCode);
        return couponByLectureList.stream()
                .map(couponByLectureMapper::toDTO)
                .collect(Collectors.toList());
    }
}
