package intbyte4.learnsmate.payment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentMonthlyRevenueDTO {
    private int year;
    private int month;
    private long totalRevenue;
}
