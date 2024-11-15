package intbyte4.learnsmate.payment.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PaymentFilterRequestVO {

    @JsonProperty("payment_code")
    private Long paymentCode;

    @JsonProperty("min_payment_price")
    private Integer minPaymentPrice;

    @JsonProperty("max_payment_price")
    private Integer maxPaymentPrice;

    @JsonProperty("start_created_at")
    private LocalDateTime startCreatedAt;

    @JsonProperty("end_created_at")
    private LocalDateTime endCreatedAt;

    @JsonProperty("lecture_code")
    private String lectureCode; // 강의코드

    @JsonProperty("lecture_title")
    private String lectureTitle; // 강의명

    @JsonProperty("min_lecture_price")
    private Integer minLecturePrice; // 강의 원가

    @JsonProperty("max_lecture_price")
    private Integer maxLecturePrice;

    @JsonProperty("tutor_code")
    private Long tutorCode; // 강사 코드

    @JsonProperty("tutor_name")
    private String tutorName; // 강사명

    @JsonProperty("student_code")
    private Long studentCode; // 학생 코드

    @JsonProperty("student_name")
    private String studentName; // 학생명

    @JsonProperty("lecture_category")
    private Integer lectureCategoryCode; // 강의 카테고리

    @JsonProperty("coupon_issuance_code")
    private String couponIssuanceCode; // 적용 쿠폰 코드

    @JsonProperty("coupon_issuance_name")
    private String couponIssuanceName; // 적용 쿠폰명
}
