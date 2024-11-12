package intbyte4.learnsmate.payment.domain.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import intbyte4.learnsmate.lecture.enums.LectureLevelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseFindPaymentVO {
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
    private Boolean lectureStatus; // 강의 상태
    private String lectureCategory; // 강의 카테고리
    private Integer lectureClickCount; // 강의 조회수
    private LectureLevelEnum lectureLevel; // 강의 난이도
    private String couponIssuanceCode; // 적용 쿠폰 코드
    private String couponIssuanceName; // 적용 쿠폰명
}
