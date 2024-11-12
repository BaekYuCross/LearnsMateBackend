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
import intbyte4.learnsmate.coupon_category.domain.dto.CouponCategoryDTO;
import intbyte4.learnsmate.coupon_category.service.CouponCategoryService;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.mapper.IssueCouponMapper;
import intbyte4.learnsmate.issue_coupon.service.IssueCouponService;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.lecture_by_student.domain.dto.LectureByStudentDTO;
import intbyte4.learnsmate.lecture_by_student.domain.entity.LectureByStudent;
import intbyte4.learnsmate.lecture_by_student.mapper.LectureByStudentMapper;
import intbyte4.learnsmate.lecture_by_student.service.LectureByStudentService;
import intbyte4.learnsmate.lecture_video_by_student.domain.dto.LectureVideoByStudentDTO;
import intbyte4.learnsmate.lecture_video_by_student.service.LectureVideoByStudentService;
import intbyte4.learnsmate.lecture_video_by_student.service.LectureVideoByStudentServiceImpl;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.payment.domain.dto.PaymentDTO;
import intbyte4.learnsmate.payment.domain.entity.Payment;
import intbyte4.learnsmate.payment.mapper.PaymentMapper;
import intbyte4.learnsmate.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final LectureService lectureService;
    private final MemberService memberService;
    private final LectureByStudentService lectureByStudentService;
    private final CouponService couponService;
    private final CouponCategoryService couponCategoryService;
    private final AdminService adminService;
    private final IssueCouponService issueCouponService;
    private final CouponMapper couponMapper;
    private final AdminMapper adminMapper;
    private final LectureVideoByStudentService lectureVideoByStudentService;

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


    // 직원이 특정 결제 내역을 단건 상세 조회
    @Override
    public PaymentDTO getPaymentDetails(Long paymentCode) {
        Payment payment = paymentRepository.findById(paymentCode)
                .orElseThrow(() -> new CommonException(StatusEnum.PAYMENT_NOT_FOUND));
        return paymentMapper.toDTO(payment);
    }

    @Override
    public List<PaymentDTO> lecturePayment(MemberDTO memberDTO, List<LectureDTO> lectureDTOList, IssueCouponDTO issueCouponDTO) {
        List<LectureDTO> selectedLectures = lectureDTOList.stream()
                .map(dto -> lectureService.getLectureById(dto.getLectureCode()))
                .toList();

        MemberDTO paidStudent = memberService
                .findMemberByMemberCode(memberDTO.getMemberCode(),memberDTO.getMemberType());
        if(paidStudent == null) throw new CommonException(StatusEnum.STUDENT_NOT_FOUND);

        Member member = memberMapper.fromMemberDTOtoMember(paidStudent);
        List<Lecture> lectureList = selectedLectures.stream()
                .map(dto -> lectureMapper.toEntity(dto, member))
                .toList();
        List<LectureByStudentDTO> lectureByStudentDTOList = new ArrayList<>();

        lectureList.forEach(lecture -> {
            LectureByStudentDTO lectureByStudentDTO = new LectureByStudentDTO();
            lectureByStudentDTO.setRefundStatus(false);
            lectureByStudentDTO.setLecture(lecture);
            lectureByStudentDTO.setStudent(member);

            lectureByStudentService.registerLectureByStudent(lectureByStudentDTO, lecture, member);

            lectureByStudentDTOList.add(lectureByStudentDTO);
        });

        CouponDTO couponDTO = couponService.findCouponByCouponCode(issueCouponDTO.getCouponCode());

        CouponCategory couponCategory = couponCategoryService.findByCouponCategoryCode(couponDTO.getCouponCategoryCode());

        AdminDTO adminDTO = adminService.findByAdminCode(couponDTO.getAdminCode());
        Admin admin = adminMapper.toEntity(adminDTO);

        MemberDTO tutorDTO = memberService.findMemberByMemberCode(couponDTO.getTutorCode(), memberDTO.getMemberType());
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);

        CouponEntity coupon = couponMapper.toEntity(couponDTO, couponCategory, admin, tutor);
        List<PaymentDTO> payments = new ArrayList<>();
        lectureList.forEach(lecture -> {
            LectureByStudentDTO lectureByStudentDTO = lectureByStudentService.findByLectureAndStudent(lecture, member);
            LectureByStudent lectureByStudent = lectureByStudentMapper.toEntity(lectureByStudentDTO, lecture, member);

            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setPaymentCode(null);
            paymentDTO.setPaymentPrice(lecture.getLecturePrice()); // 얘를 수정해야함. 쿠폰 적용되면 할인된 가격 들어가게. lectureService.가격(강의코드);
            paymentDTO.setCreatedAt(LocalDateTime.now());
            paymentDTO.setLectureByStudentCode(lectureByStudentService.findStudentCodeByLectureCode(lecture));
            paymentDTO.setCouponIssuanceCode(issueCouponDTO.getCouponIssuanceCode());

            Payment payment = paymentMapper.toEntity(paymentDTO, lectureByStudent, issueCouponMapper
                    .toEntity(issueCouponDTO, member, coupon));

            paymentRepository.save(payment);

            payments.add(paymentDTO);
        });
        //학생별강의동영상.저장메서드()
        lectureByStudentDTOList.forEach(lectureByStudentDTO -> {
            LectureVideoByStudentDTO lectureVideoByStudentDTO = new LectureVideoByStudentDTO();
            lectureVideoByStudentDTO.setVideoCode(null);
            lectureVideoByStudentDTO.setLectureByStudentCode(lectureByStudentDTO.getLectureByStudentCode());
            lectureVideoByStudentDTO.setLectureStatus(false);
        });
        return payments;
    }
    // 직원이 예상 매출액과 할인 매출액을 비교해서 조회

    // 직원이 기간 별 매출액을 리스트와 그래프로 조회

}
