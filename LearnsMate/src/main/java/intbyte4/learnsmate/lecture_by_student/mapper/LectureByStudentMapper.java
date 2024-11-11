package intbyte4.learnsmate.lecture_by_student.mapper;


import intbyte4.learnsmate.lecture_by_student.domain.dto.LectureByStudentDTO;
import intbyte4.learnsmate.lecture_by_student.domain.entity.LectureByStudent;
import org.springframework.stereotype.Component;

@Component
public class LectureByStudentMapper {

    public LectureByStudentDTO toDTO(LectureByStudent entity) {
        return LectureByStudentDTO.builder()
                .lectureByStudentCode(entity.getLectureByStudentCode())
                .refundStatus(entity.getRefundStatus())
                .lecture(entity.getLecture())
                .student(entity.getStudent())
                .build();
    }

}
