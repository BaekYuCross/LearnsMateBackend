package intbyte4.learnsmate.payment.domain.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestRegisterPaymentVO {
    private RequestRegisterIssueCouponPaymentVO issueCouponVO;
    private RequestRegisterMemberPaymentVO memberVO;
    private List<RequestRegisterLecturePaymentVO> LectureVOList;

    private Long paymentCode;
    private Integer paymentPrice;
    private LocalDateTime createdAt;
    private Long lectureByStudentCode;
    private String couponIssuanceCode;
}
