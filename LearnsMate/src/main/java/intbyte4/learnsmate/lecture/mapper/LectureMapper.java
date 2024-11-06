package intbyte4.learnsmate.lecture.mapper;

import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.domain.vo.response.ResponseFindLectureVO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.domain.vo.request.RequestSaveMemberVO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LectureMapper {

    public LectureDTO toDTO(Lecture entity) {
        return LectureDTO.builder()
                .lectureCode(entity.getLectureCode())
                .lectureTitle(entity.getLectureTitle())
                .lectureCategoryEnum(entity.getLectureCategoryEnum())
                .lectureConfirmStatus(entity.getLectureConfirmStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .lectureImage(entity.getLectureImage())
                .lecturePrice(entity.getLecturePrice())
                .lectureStatus(entity.getLectureStatus())
                .lectureClickCount(entity.getLectureClickCount())
                .lectureLevel(entity.getLectureLevel())
                .tutorCode(entity.getTutorCode().getMemberCode())
                .build();

    }

    public Lecture toEntity(LectureDTO dto, Member tutor) {
        return Lecture.builder()
                .lectureCode(dto.getLectureCode())
                .lectureTitle(dto.getLectureTitle())
                .lectureCategoryEnum(dto.getLectureCategoryEnum())
                .lectureConfirmStatus(dto.getLectureConfirmStatus())
                .createdAt(dto.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .lectureImage(dto.getLectureImage())
                .lecturePrice(dto.getLecturePrice())
                .lectureStatus(dto.getLectureStatus())
                .lectureClickCount(dto.getLectureClickCount())
                .lectureLevel(dto.getLectureLevel())
                .tutorCode(tutor)
                .build();
    }

    public ResponseFindLectureVO fromEntityToResponseVO(Lecture entity) {
        return ResponseFindLectureVO.builder()
                .lectureCode(entity.getLectureCode())
                .lectureTitle(entity.getLectureTitle())
                .lectureCategoryEnum(entity.getLectureCategoryEnum())
                .lectureConfirmStatus(entity.getLectureConfirmStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .lectureImage(entity.getLectureImage())
                .lecturePrice(entity.getLecturePrice())
                .lectureStatus(entity.getLectureStatus())
                .lectureClickCount(entity.getLectureClickCount())
                .lectureLevel(entity.getLectureLevel())
                .tutorCode(entity.getTutorCode().getMemberCode())
                .build();
    }


}
