package intbyte4.learnsmate.payment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMonthlyRevenueDTO {
    private int year;
    private int month;
    private long totalRevenue;
}
