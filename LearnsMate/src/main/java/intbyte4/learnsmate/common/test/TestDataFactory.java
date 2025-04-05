package intbyte4.learnsmate.common.test;


import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.repository.CouponRepository;
import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.coupon_category.repository.CouponCategoryRepository;
import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import intbyte4.learnsmate.issue_coupon.repository.IssueCouponRepository;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.repository.LectureRepository;
import intbyte4.learnsmate.lecture_by_student.domain.entity.LectureByStudent;
import intbyte4.learnsmate.lecture_by_student.repository.LectureByStudentRepository;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.repository.MemberRepository;
import intbyte4.learnsmate.payment.domain.entity.Payment;
import intbyte4.learnsmate.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TestDataFactory {

    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final LectureByStudentRepository lectureByStudentRepository;
    private final CouponRepository couponRepository;
    private final IssueCouponRepository issueCouponRepository;
    private final PaymentRepository paymentRepository;
    private final CouponCategoryRepository couponCategoryRepository;

    public Member createTestMember(String name, MemberType type) {
        return memberRepository.save(
                Member.builder()
                        .memberType(type)
                        .memberEmail(name + "@test.com")
                        .memberPassword("1234")
                        .memberName(name)
                        .memberAge(25)
                        .memberPhone("010-1111-2222")
                        .memberAddress("서울시 테스트구")
                        .memberBirth(LocalDateTime.of(2000, 1, 1, 0, 0))
                        .memberFlag(true)
                        .memberDormantStatus(false)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );
    }

    public Lecture createTestLecture(Member tutor) {
        return lectureRepository.save(
                Lecture.builder()
                        .lectureCode(UUID.randomUUID().toString().substring(0, 8))
                        .lectureTitle("테스트 강의")
                        .lectureConfirmStatus(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .lectureImage("https://example.com/image.jpg")
                        .lecturePrice(15000)
                        .tutor(tutor)
                        .lectureStatus(true)
                        .lectureClickCount(0)
                        .lectureLevel(intbyte4.learnsmate.lecture.domain.entity.LectureLevelEnum.BEGINNER)
                        .build()
        );
    }

    public LectureByStudent createTestLectureByStudent(Member student, Lecture lecture) {
        return lectureByStudentRepository.save(
                LectureByStudent.builder()
                        .ownStatus(true)
                        .lecture(lecture)
                        .student(student)
                        .build()
        );
    }

    public CouponEntity createTestCoupon(Member tutor, CouponCategory category) {
        return couponRepository.save(
                CouponEntity.builder()
                        .couponName("10% 할인 쿠폰")
                        .couponContents("테스트 쿠폰입니다.")
                        .couponDiscountRate(10)
                        .couponStartDate(LocalDateTime.now().minusDays(1))
                        .couponExpireDate(LocalDateTime.now().plusDays(7))
                        .couponFlag(true)
                        .activeState(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .couponCategory(category)
                        .tutor(tutor) // 또는 admin(adminUser)
                        .build()
        );
    }

    public IssueCoupon createTestIssueCoupon(Member student, CouponEntity coupon) {
        IssueCoupon issueCoupon = IssueCoupon.createIssueCoupon(coupon, student);
        return issueCouponRepository.save(issueCoupon);
    }

    public Payment createTestPaymentWithCoupon() {
        Member tutor = createTestMember("튜터", MemberType.TUTOR);
        Member student = createTestMember("학생", MemberType.STUDENT);
        Lecture lecture = createTestLecture(tutor);
        LectureByStudent lbs = createTestLectureByStudent(student, lecture);
        CouponCategory category = couponCategoryRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("카테고리 없음"));
        CouponEntity coupon = createTestCoupon(tutor, category);
        IssueCoupon issueCoupon = createTestIssueCoupon(student, coupon);

        return paymentRepository.save(
                Payment.builder()
                        .lectureByStudent(lbs)
                        .couponIssuance(issueCoupon)
                        .paymentPrice(15000)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    public Payment createTestPaymentWithoutCoupon() {
        Member tutor = createTestMember("튜터", MemberType.TUTOR);
        Member student = createTestMember("학생", MemberType.STUDENT);
        Lecture lecture = createTestLecture(tutor);
        LectureByStudent lbs = createTestLectureByStudent(student, lecture);

        return paymentRepository.save(
                Payment.builder()
                        .lectureByStudent(lbs)
                        .couponIssuance(null)
                        .paymentPrice(15000)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }
}
