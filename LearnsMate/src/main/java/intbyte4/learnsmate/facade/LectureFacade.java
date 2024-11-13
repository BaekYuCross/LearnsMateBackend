package intbyte4.learnsmate.facade;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.mapper.CouponMapper;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.coupon_by_lecture.domain.dto.CouponByLectureDTO;
import intbyte4.learnsmate.coupon_by_lecture.service.CouponByLectureService;
import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.coupon_category.service.CouponCategoryServiceImpl;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.mapper.IssueCouponMapper;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LectureFacade {
    private final CouponService couponService;
    private final CouponByLectureService couponByLectureService;
    private final LectureMapper lectureMapper;
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final IssueCouponMapper issueCouponMapper;
    private final CouponMapper couponMapper;
    private final CouponCategoryServiceImpl couponCategoryService;
    private final AdminService adminService;
    private final AdminMapper adminMapper;

    @Transactional
    public LectureDTO discountLecturePrice(LectureDTO lectureDTO, IssueCouponDTO issueCouponDTO) {
        MemberDTO tutorDTO = memberService.findById(lectureDTO.getTutorCode());
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);

        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor);

        MemberDTO studentDTO = memberService.findById(issueCouponDTO.getStudentCode());
        Member student = memberMapper.fromMemberDTOtoMember(studentDTO);

        CouponDTO couponDTO = couponService.findCouponDTOByCouponCode(issueCouponDTO.getCouponCode());
        // 쿠폰카테고리디티오로 반환하는게 필요
        CouponCategory couponCategory = couponCategoryService.findByCouponCategoryCode(couponDTO.getCouponCategoryCode());

        AdminDTO adminDTO = adminService.findByAdminCode(couponDTO.getAdminCode());
        Admin admin = adminMapper.toEntity(adminDTO);
        CouponEntity coupon = couponMapper.toEntity(couponDTO,couponCategory,admin,tutor);

        CouponByLectureDTO couponByLectureDTO = couponByLectureService.findByCouponAndLecture(lecture, coupon);
        if(couponByLectureDTO == null) return lectureDTO;
        lectureDTO.setLecturePrice(lectureDTO.getLecturePrice() * (1 - couponDTO.getCouponDiscountRate() / 100));
        return lectureDTO;
    }
}
