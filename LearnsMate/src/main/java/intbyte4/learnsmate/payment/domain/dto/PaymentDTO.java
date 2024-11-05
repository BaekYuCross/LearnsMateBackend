package intbyte4.learnsmate.payment.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long paymentCode;
    private Integer paymentPrice;
    private LocalDateTime createdAt;
    private Long lectureByStudentCode;
    private Long couponIssuanceCode;

}
