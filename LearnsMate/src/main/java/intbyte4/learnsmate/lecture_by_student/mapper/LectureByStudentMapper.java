package intbyte4.learnsmate.lecture_by_student.mapper;


import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture_by_student.domain.dto.LectureByStudentDTO;
import intbyte4.learnsmate.lecture_by_student.domain.entity.LectureByStudent;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.payment.domain.dto.PaymentDTO;
import intbyte4.learnsmate.payment.domain.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class LectureByStudentMapper {

    public LectureByStudentDTO toDTO(LectureByStudent entity) {
        return LectureByStudentDTO.builder()
                .lectureByStudentCode(entity.getLectureByStudentCode())
                .ownStatus(entity.getOwnStatus())
                .lecture(entity.getLecture())
                .student(entity.getStudent())
                .build();
    }

    public LectureByStudent toEntity(LectureByStudentDTO lectureByStudentDTO, Lecture lecture, Member student) {
        return LectureByStudent.builder()
                .lectureByStudentCode(lectureByStudentDTO.getLectureByStudentCode())
                .refundStatus(lectureByStudentDTO.getRefundStatus())
                .lecture(lecture)
                .student(student)
                .build();
    }

}
