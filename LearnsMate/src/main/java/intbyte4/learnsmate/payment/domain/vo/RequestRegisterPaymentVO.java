package intbyte4.learnsmate.payment.domain.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
public class RequestRegisterPaymentVO {
    private RequestRegisterIssueCouponPaymentVO issueCouponVO;
    private RequestRegisterMemberPaymentVO memberVO;
    private RequestRegisterLecturePaymentVO LectureVO;
    private Integer paymentPrice;
}
