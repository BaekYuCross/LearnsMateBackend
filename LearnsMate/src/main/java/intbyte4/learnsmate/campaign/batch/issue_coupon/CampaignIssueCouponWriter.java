package intbyte4.learnsmate.campaign.batch.issue_coupon;

import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CampaignIssueCouponWriter implements ItemWriter<IssueCoupon> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void write(Chunk<? extends IssueCoupon> chunk) throws Exception {
        List<IssueCoupon> coupons = new ArrayList<>(chunk.getItems());

        String sql = """
            INSERT INTO issue_coupon (
                coupon_issuance_code,
                coupon_issue_date,
                coupon_use_status,
                coupon_use_date,
                student_code,
                coupon_code
            )
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(java.sql.PreparedStatement ps, int i) throws SQLException {
                IssueCoupon ic = coupons.get(i);
                ps.setString(1, ic.getCouponIssuanceCode());
                ps.setObject(2, ic.getCouponIssueDate());
                ps.setBoolean(3, ic.getCouponUseStatus());
                if (ic.getCouponUseDate() != null) {
                    ps.setObject(4, ic.getCouponUseDate());
                } else {
                    ps.setNull(4, java.sql.Types.TIMESTAMP);
                }
                ps.setLong(5, ic.getStudent().getMemberCode());
                ps.setLong(6, ic.getCoupon().getCouponCode());
            }

            @Override
            public int getBatchSize() {
                return coupons.size();
            }
        });
    }
}