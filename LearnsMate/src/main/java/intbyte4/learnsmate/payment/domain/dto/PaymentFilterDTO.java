package intbyte4.learnsmate.payment.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFilterDTO {
    private Long paymentCode;
    private Integer paymentPrice;
    private LocalDateTime createdAt;
    private Long lectureCode; // 강의코드
    private String lectureTitle; // 강의명
    private Integer lecturePrice; // 강의 원가
    private Long tutorCode; // 강사 코드
    private String tutorName; // 강사 명
    private Long studentCode; // 학생 코드
    private String studentName; // 학생 명
    private Integer lectureCategoryCode; // 강의 카테고리
    private String couponIssuanceCode; // 적용 쿠폰 코드
    private String couponIssuanceName; // 적용 쿠폰명
}
