package intbyte4.learnsmate.lecturebystudent.mapper;


import intbyte4.learnsmate.lecturebystudent.domain.dto.LectureByStudentDTO;
import intbyte4.learnsmate.lecturebystudent.domain.entity.LectureByStudent;
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
