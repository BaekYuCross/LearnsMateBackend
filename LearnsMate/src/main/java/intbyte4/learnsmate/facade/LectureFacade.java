package intbyte4.learnsmate.facade;

import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LectureFacade {
    private final CouponService couponService;

    @Transactional
    public LectureDTO discountLecturePrice(LectureDTO lectureDTO, IssueCouponDTO issueCouponDTO) {
        if(issueCouponDTO==null){
            return lectureDTO;
        }
        CouponEntity coupon = couponService.findByCouponCode(issueCouponDTO.getCouponCode());
        lectureDTO.setLecturePrice(lectureDTO.getLecturePrice() * (1 - coupon.getCouponDiscountRate() / 100));
        return lectureDTO;
    }

    // 1. 멤버, 쿠폰, 강의
    // 강의 쿠폰? 가져와야함. -> 쿠폰 디티오 가져옴 -> 쿠폰에 멤버코드가 있음.
    // 멤버코드로 멤버 가져옴
    // 끄,ㅌ임 ㅇㅈ?
    // 하나의 파사드로 해결 가능 ㅇㅋ? 대ㅔ대ㅏㅂ대답대답대답
    // 근데 리딩 서비스를 활용한다? 모든게 말끔하게 해결되지요?
}
