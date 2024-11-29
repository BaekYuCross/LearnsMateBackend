package intbyte4.learnsmate.payment.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaymentPageResponse<T, G> {
    private List<T> paymentData;
    private G graphData;
    private boolean hasNext;
    private long totalElements;
}
