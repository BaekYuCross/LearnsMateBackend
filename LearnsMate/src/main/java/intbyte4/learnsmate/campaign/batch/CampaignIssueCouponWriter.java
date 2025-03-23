package intbyte4.learnsmate.campaign.batch;

import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import intbyte4.learnsmate.issue_coupon.repository.IssueCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CampaignIssueCouponWriter implements ItemWriter<IssueCoupon> {

    private final IssueCouponRepository issueCouponRepository;

    @Override
    public void write(Chunk<? extends IssueCoupon> chunk) throws Exception {
        long startTime = System.currentTimeMillis(); // 시작 시간 기록

        List<IssueCoupon> issueCoupons = new ArrayList<>();
        for (IssueCoupon issueCoupon : chunk) {
            IssueCoupon newIssueCoupon = IssueCoupon.createIssueCoupon(issueCoupon.getCoupon(), issueCoupon.getStudent());
            issueCoupons.add(newIssueCoupon);
        }
        issueCouponRepository.saveAll(issueCoupons);

        long endTime = System.currentTimeMillis(); // 종료 시간 기록
        log.info("Writer 데이터 쓰기 완료: {}ms, 처리된 데이터 수: {}", endTime - startTime, issueCoupons.size());
    }
}