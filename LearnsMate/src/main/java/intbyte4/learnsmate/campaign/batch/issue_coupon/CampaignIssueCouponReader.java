package intbyte4.learnsmate.campaign.batch.issue_coupon;

import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CampaignIssueCouponReader implements ItemReader<Pair<MemberDTO, CouponDTO>> {

    private List<MemberDTO> students;
    private List<CouponDTO> coupons;
    private int studentIndex = 0;
    private int couponIndex = 0;

    public void setStudentCouponPairs(List<MemberDTO> students, List<CouponDTO> coupons) {
        this.students = students;
        this.coupons = coupons;
        this.studentIndex = 0;
        this.couponIndex = 0;
    }

    @Override
    public Pair<MemberDTO, CouponDTO> read() {
        if (students == null || coupons == null || students.isEmpty() || coupons.isEmpty()) {
            return null;
        }

        if (studentIndex >= students.size()) {
            return null;
        }

        Pair<MemberDTO, CouponDTO> pair = Pair.of(
                students.get(studentIndex),
                coupons.get(couponIndex)
        );

        couponIndex++;

        if (couponIndex >= coupons.size()) {
            couponIndex = 0;
            studentIndex++;
        }

        return pair;
    }
}
