package intbyte4.learnsmate.payment.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.mapper.CouponMapper;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.coupon_category.service.CouponCategoryService;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.mapper.IssueCouponMapper;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture_by_student.domain.dto.LectureByStudentDTO;
import intbyte4.learnsmate.lecture_by_student.domain.entity.LectureByStudent;
import intbyte4.learnsmate.lecture_by_student.mapper.LectureByStudentMapper;
import intbyte4.learnsmate.lecture_by_student.service.LectureByStudentService;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategoryEnum;
import intbyte4.learnsmate.lecture_video_by_student.domain.dto.LectureVideoByStudentDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.payment.domain.dto.PaymentDTO;
import intbyte4.learnsmate.payment.domain.dto.PaymentFilterDTO;
import intbyte4.learnsmate.payment.domain.entity.Payment;
import intbyte4.learnsmate.payment.domain.vo.PaymentFilterRequestVO;
import intbyte4.learnsmate.payment.mapper.PaymentMapper;
import intbyte4.learnsmate.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final LectureMapper lectureMapper;
    private final MemberMapper memberMapper;
    private final LectureByStudentMapper lectureByStudentMapper;
    private final IssueCouponMapper issueCouponMapper;
    private final MemberService memberService;
    private final LectureByStudentService lectureByStudentService;
    private final CouponService couponService;
    private final CouponCategoryService couponCategoryService;
    private final AdminService adminService;
    private final CouponMapper couponMapper;
    private final AdminMapper adminMapper;

    // payment facade에 있는 조회반환 값이랑 달라서 통계에 사용할 수 있으므로 남겨둠 지우지 마셈.
    // 직원이 전체 결제 내역을 조회
    @Override
    public List<PaymentDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        if (payments.isEmpty()) {
            throw new CommonException(StatusEnum.PAYMENT_NOT_FOUND);
        }
        return payments.stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentFilterDTO> getPaymentsByFilters(PaymentFilterRequestVO request) {
        return paymentRepository.findPaymentByFilters(request);
    }

    // 직원이 특정 결제 내역을 단건 상세 조회
    @Override
    public PaymentDTO getPaymentDetails(Long paymentCode) {
        Payment payment = paymentRepository.findById(paymentCode)
                .orElseThrow(() -> new CommonException(StatusEnum.PAYMENT_NOT_FOUND));
        return paymentMapper.toDTO(payment);
    }

    @Override
    public PaymentDTO lectureAdaptedPayment(MemberDTO memberDTO, LectureDTO lectureDTO, IssueCouponDTO issueCouponDTO) {
        Result result = getResult(memberDTO, lectureDTO);

        CouponDTO couponDTO = couponService.findCouponDTOByCouponCode(issueCouponDTO.getCouponCode());
        CouponCategory couponCategory = couponCategoryService.findByCouponCategoryCode(couponDTO.getCouponCategoryCode());

        AdminDTO adminDTO = adminService.findByAdminCode(couponDTO.getAdminCode());

        PaymentDTO paymentDTO;

        if (adminDTO == null) {
            CouponEntity coupon = couponMapper.toTutorCouponEntity(couponDTO, couponCategory, result.tutor());
            LectureByStudent lectureByStudent = lectureByStudentMapper.toEntity(result.lectureByStudentDTO(), result.lecture(), result.student());

            paymentDTO = getPaymentDTO(lectureDTO, issueCouponDTO, result.lecture(), result.lectureByStudentDTO(), lectureByStudent, result.student(), coupon);
        } else {
            Admin admin = adminMapper.toEntity(adminDTO);
            CouponEntity coupon = couponMapper.toAdminCouponEntity(couponDTO, couponCategory, admin);
            LectureByStudent lectureByStudent = lectureByStudentMapper.toEntity(result.lectureByStudentDTO(), result.lecture(), result.student());

            paymentDTO = getPaymentDTO(lectureDTO, issueCouponDTO, result.lecture(), result.lectureByStudentDTO(), lectureByStudent, result.student(), coupon);
        }
        return paymentDTO;
    }

    @Override
    public PaymentDTO lectureUnAdaptedPayment(MemberDTO memberDTO, LectureDTO lectureDTO) {
        Member student = memberMapper.fromMemberDTOtoMember(memberDTO);
        MemberDTO tutorDTO = memberService.findById(lectureDTO.getTutorCode());
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);
        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor);

        LectureByStudentDTO lectureByStudentDTO = new LectureByStudentDTO();
        lectureByStudentService.registerLectureByStudent(lectureByStudentDTO, lecture, student);
        LectureByStudent lectureByStudent = lectureByStudentMapper.toEntity(lectureByStudentDTO, lecture, student);

        PaymentDTO paymentDTO = getPaymentDTO(lectureDTO, lecture);
        Payment payment = paymentMapper.toEntity(paymentDTO, lectureByStudent);

        paymentRepository.save(payment);

        return paymentDTO;
    }

    @Override
    public int getPurchaseCountByLectureCode(String lectureCode) {
        return paymentRepository.countPaymentsByLectureCode(lectureCode);
    }

    @Override
    public String findLatestLectureCodeByStudent(Long studentCode) {
        Pageable pageable = PageRequest.of(0, 1); // 최신 1개의 강의만 가져옴
        List<String> lectureCodes = paymentRepository.findLectureCodesByStudent(studentCode, pageable);

        if(lectureCodes.isEmpty()) return null;
        return lectureCodes.get(0); // 가장 최신 강의 코드 반환
    }

    @Override
    public List<Object[]> findRecommendedLectures(List<Long> similarStudents, String latestLectureCode, Long studentCode, Pageable pageable) {
        return paymentRepository.findRecommendedLectures(similarStudents, latestLectureCode, studentCode, pageable);
    }

    @Override
    public Integer getTotalStudentCountBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.countDistinctStudentsBetweenDates(startDate, endDate);
    }

    @Override
    public Integer getStudentCountByLectureCodeBetween(String lectureCode, LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.countDistinctStudentsByLectureCodeBetweenDates(lectureCode, startDate, endDate);
    }

    @Override
    public int getTotalPurchaseCount() {
        return paymentRepository.findTotalPurchaseCount();
    }

    @Override
    public int getPurchaseCountByCategory(String lectureCategoryName) {
        return paymentRepository.findPurchaseCountByCategory(LectureCategoryEnum.valueOf(lectureCategoryName));
    }

    @Override
    public Integer getPurchaseCountByCategoryWithDateRange(String categoryName, LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findPurchaseCountByCategoryWithDateRange(LectureCategoryEnum.valueOf(categoryName), startDate, endDate);
    }


    private Result getResult(MemberDTO memberDTO, LectureDTO lectureDTO) {
        Member student = memberMapper.fromMemberDTOtoMember(memberDTO);
        MemberDTO tutorDTO = memberService.findById(lectureDTO.getTutorCode());
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);
        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor);

        LectureByStudentDTO lectureByStudentDTO = new LectureByStudentDTO();
        lectureByStudentService.registerLectureByStudent(lectureByStudentDTO, lecture, student);
        return new Result(student, tutor, lecture, lectureByStudentDTO);
    }

    private record Result(Member student, Member tutor, Lecture lecture, LectureByStudentDTO lectureByStudentDTO) {
    }

    private PaymentDTO getPaymentDTO(LectureDTO lectureDTO, IssueCouponDTO issueCouponDTO, Lecture lecture, LectureByStudentDTO lectureByStudentDTO, LectureByStudent lectureByStudent, Member student, CouponEntity coupon) {
        PaymentDTO paymentDTO = getPaymentDTO(lectureDTO, lecture);
        if (issueCouponDTO.getStudentCode().equals(lectureByStudentDTO.getStudent().getMemberCode())) {
            paymentDTO.setCouponIssuanceCode(issueCouponDTO.getCouponIssuanceCode());
        }

        Payment payment = paymentMapper.toAdaptEntity(paymentDTO, lectureByStudent, issueCouponMapper.toEntity(issueCouponDTO, student, coupon));
        paymentRepository.save(payment);

        setLectureByStudentDTO(lectureByStudentDTO);
        return paymentDTO;
    }

    private void setLectureByStudentDTO(LectureByStudentDTO lectureByStudentDTO) {
        LectureVideoByStudentDTO lectureVideoByStudentDTO = new LectureVideoByStudentDTO();
        lectureVideoByStudentDTO.setVideoCode(null);
        lectureVideoByStudentDTO.setLectureByStudentCode(lectureByStudentDTO.getLectureByStudentCode());
        lectureVideoByStudentDTO.setLectureStatus(false);
    }

    private PaymentDTO getPaymentDTO(LectureDTO lectureDTO, Lecture lecture) {
        PaymentDTO paymentInfo = new PaymentDTO();
        paymentInfo.setPaymentCode(null);
        paymentInfo.setPaymentPrice(lectureDTO.getLecturePrice());
        paymentInfo.setCreatedAt(LocalDateTime.now());
        paymentInfo.setLectureByStudentCode(lectureByStudentService.findStudentCodeByLectureCode(lecture));
        return paymentInfo;
    }
}
