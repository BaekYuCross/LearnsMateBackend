package intbyte4.learnsmate.campaign.batch;

import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CampaignIssueCouponReader implements ItemReader<Pair<MemberDTO, CouponDTO>> {
    private List<Pair<MemberDTO, CouponDTO>> studentCouponPairs;
    private Iterator<Pair<MemberDTO, CouponDTO>> iterator;

    public void setStudentCouponPairs(List<MemberDTO> students, List<CouponDTO> coupons) {
        this.studentCouponPairs = new ArrayList<>();
        for (MemberDTO student : students) {
            for (CouponDTO coupon : coupons) {
                studentCouponPairs.add(Pair.of(student, coupon));
            }
        }
        this.iterator = studentCouponPairs.iterator();
    }

    @Override
    public Pair<MemberDTO, CouponDTO> read() {
        if (iterator != null && iterator.hasNext()) {
            return iterator.next();
        } else {
            return null;
        }
    }
}

