package intbyte4.learnsmate.campaign.batch;

import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import intbyte4.learnsmate.issue_coupon.repository.IssueCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CampaignIssueCouponWriter implements ItemWriter<IssueCoupon> {

    private final IssueCouponRepository issueCouponRepository;

    @Override
    public void write(Chunk<? extends IssueCoupon> chunk) throws Exception {
        List<IssueCoupon> issueCoupons = new ArrayList<>();

        for (IssueCoupon issueCoupon : chunk) {
            IssueCoupon newIssueCoupon = IssueCoupon.createIssueCoupon(issueCoupon.getCoupon(), issueCoupon.getStudent());
            issueCoupons.add(newIssueCoupon);
        }
        issueCouponRepository.saveAll(issueCoupons);
    }
}
