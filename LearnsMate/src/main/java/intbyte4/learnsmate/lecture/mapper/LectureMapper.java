package intbyte4.learnsmate.lecture.mapper;

import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.domain.vo.response.ResponseEditLectureInfoVO;
import intbyte4.learnsmate.lecture.domain.vo.response.ResponseFindLectureVO;
import intbyte4.learnsmate.lecture.domain.vo.response.ResponseRemoveLectureVO;
import intbyte4.learnsmate.member.domain.entity.Member;
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

    // DTO -> VO 변환
    public ResponseFindLectureVO fromDtoToResponseVO(LectureDTO dto) {
        return ResponseFindLectureVO.builder()
                .lectureCode(dto.getLectureCode())
                .lectureTitle(dto.getLectureTitle())
                .lectureCategoryEnum(dto.getLectureCategoryEnum())
                .lectureConfirmStatus(dto.getLectureConfirmStatus())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .lectureImage(dto.getLectureImage())
                .lecturePrice(dto.getLecturePrice())
                .lectureStatus(dto.getLectureStatus())
                .lectureClickCount(dto.getLectureClickCount())
                .lectureLevel(dto.getLectureLevel())
                .tutorCode(dto.getTutorCode())
                .build();
    }


    public ResponseEditLectureInfoVO fromDtoToEditResponseVO(LectureDTO updatedLecture) {
        return ResponseEditLectureInfoVO.builder()
                .lectureTitle(updatedLecture.getLectureTitle())
                .lectureCategoryEnum(updatedLecture.getLectureCategoryEnum())
                .lectureConfirmStatus(updatedLecture.getLectureConfirmStatus())
                .updatedAt(updatedLecture.getUpdatedAt())
                .lectureImage(updatedLecture.getLectureImage())
                .lecturePrice(updatedLecture.getLecturePrice())
                .lectureStatus(updatedLecture.getLectureStatus())
                .lectureClickCount(updatedLecture.getLectureClickCount())
                .lectureLevel(updatedLecture.getLectureLevel())
                .build();
    }

    public ResponseRemoveLectureVO fromDtoToRemoveResponseVO(LectureDTO removedLecture) {
        return ResponseRemoveLectureVO.builder()
                .lectureCode(removedLecture.getLectureCode())
                .lectureTitle(removedLecture.getLectureTitle())
                .lectureCategoryEnum(removedLecture.getLectureCategoryEnum())
                .lectureConfirmStatus(removedLecture.getLectureConfirmStatus())
                .createdAt(removedLecture.getCreatedAt())
                .updatedAt(removedLecture.getUpdatedAt())
                .lectureImage(removedLecture.getLectureImage())
                .lecturePrice(removedLecture.getLecturePrice())
                .tutorCode(removedLecture.getTutorCode())
                .lectureStatus(removedLecture.getLectureStatus())
                .lectureClickCount(removedLecture.getLectureClickCount())
                .lectureLevel(removedLecture.getLectureLevel())
                .build();
    }
}
