package intbyte4.learnsmate.coupon_by_lecture.domain.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponByLectureFindResponseVO {

    @JsonProperty("coupon_by_lecture_code")
    private Long couponByLectureCode;

    @JsonProperty("coupon_code")
    private Long couponCode;

    @JsonProperty("lecture_code")
    private Long lectureCode;

}
