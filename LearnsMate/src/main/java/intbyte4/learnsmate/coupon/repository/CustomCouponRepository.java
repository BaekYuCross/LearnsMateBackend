package intbyte4.learnsmate.coupon.repository;

import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.domain.vo.request.CouponFilterRequestVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCouponRepository {

    List<CouponEntity> findCouponsByFilters(CouponFilterRequestVO request);

    Page<CouponEntity> searchBy(CouponFilterRequestVO request, Pageable pageable);

    // 필터링o 정렬o
    Page<CouponEntity> searchByWithSort(CouponFilterRequestVO request, Pageable pageable);
}
