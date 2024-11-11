package intbyte4.learnsmate.coupon_by_lecture.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CouponByLectureDTO {
    private Long couponByLectureCode;
    private Long couponCode;
    private Long lectureCode;
}
