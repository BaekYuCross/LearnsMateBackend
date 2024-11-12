package intbyte4.learnsmate.payment.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.service.IssueCouponService;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.lecture_by_student.domain.dto.LectureByStudentDTO;
import intbyte4.learnsmate.lecture_by_student.service.LectureByStudentService;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final LectureMapper lectureMapper;
    private final MemberMapper memberMapper;
    private final LectureService lectureService;
    private final MemberService memberService;
    private final LectureByStudentService lectureByStudentService;
    private final IssueCouponService issueCouponService;

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
    public PaymentDTO lecturePayment(MemberDTO memberDTO, List<LectureDTO> lectureDTOList, IssueCouponDTO issueCouponDTO) {
        List<LectureDTO> selectedLectures = lectureDTOList.stream()
                .map(dto -> lectureService.getLectureById(dto.getLectureCode()))
                .toList();

        MemberDTO paidStudent = memberService
                .findMemberByMemberCode(memberDTO.getMemberCode(),memberDTO.getMemberType());
        if(paidStudent == null) throw new CommonException(StatusEnum.STUDENT_NOT_FOUND);

        Member member = memberMapper.fromMemberDTOtoMember(memberDTO);
        List<Lecture> lectureList = selectedLectures.stream()
                .map(dto -> lectureMapper.toEntity(dto, member))
                .toList();

        lectureList.forEach(lecture -> {
            LectureByStudentDTO lectureByStudentDTO = new LectureByStudentDTO();
            lectureByStudentDTO.setRefundStatus(false);
            lectureByStudentDTO.setLecture(lecture);
            lectureByStudentDTO.setStudent(member);

            lectureByStudentService.registerLectureByStudent(lectureByStudentDTO, lecture, member);
        });

        lectureList.forEach(lecture -> {
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setPaymentCode(null);
            paymentDTO.setPaymentPrice(lecture.getLecturePrice());
            paymentDTO.setCreatedAt(LocalDateTime.now());
            paymentDTO.setLectureByStudentCode(lectureByStudentService.findStudentCodeByLectureCode(lecture));
            paymentDTO.setCouponIssuanceCode(issueCouponDTO.getCouponIssuanceCode());
            // 또 매퍼로 바꿔야됨 ㅇㅇ
//            paymentRepository.save();
        });

        //결제레포.세이브()
        //학생별강의동영상.저장메서드()
        //
        //리턴 결제디티오

        return null;
    }
    // 직원이 예상 매출액과 할인 매출액을 비교해서 조회

    // 직원이 기간 별 매출액을 리스트와 그래프로 조회

}
