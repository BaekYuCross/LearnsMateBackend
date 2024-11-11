package intbyte4.learnsmate.coupon_by_lecture.mapper;

import intbyte4.learnsmate.coupon_by_lecture.domain.CouponByLecture;
import intbyte4.learnsmate.coupon_by_lecture.domain.dto.CouponByLectureDTO;
import intbyte4.learnsmate.coupon_by_lecture.domain.vo.response.CouponByLectureFindResponseVO;
import org.springframework.stereotype.Component;

@Component
public class CouponByLectureMapper {
    public CouponByLectureFindResponseVO fromDtoToFindResponseVO(CouponByLectureDTO couponByLectureDTO) {
        return CouponByLectureFindResponseVO.builder()
                .couponByLectureCode(couponByLectureDTO.getCouponByLectureCode())
                .couponCode(couponByLectureDTO.getCouponCode())
                .lectureCode(couponByLectureDTO.getLectureCode())
                .build();
    }

    public CouponByLectureDTO toDTO(CouponByLecture couponByLecture) {
        return CouponByLectureDTO.builder()
                .couponByLectureCode(couponByLecture.getCouponByLectureCode())
                .couponCode(couponByLecture.getCoupon().getCouponCode())
                .lectureCode(couponByLecture.getLecture().getLectureCode())
                .build();
    }
}
