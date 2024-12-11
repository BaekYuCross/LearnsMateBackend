package intbyte4.learnsmate.lecture_category.mapper;

import intbyte4.learnsmate.lecture_category.domain.dto.LectureCategoryDTO;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategory;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategoryEnum;
import org.springframework.stereotype.Component;

@Component
public class LectureCategoryMapper {

    // 엔티티 -> DTO 변환
    public LectureCategoryDTO toDTO(LectureCategory lectureCategory) {
        return LectureCategoryDTO.builder()
                .lectureCategoryCode(lectureCategory.getLectureCategoryCode())
                .lectureCategoryName(String.valueOf(lectureCategory.getLectureCategoryName()))
                .build();
    }

    // DTO -> 엔티티 변환
    public LectureCategory toEntity(LectureCategoryDTO lectureCategoryDTO) {
        return LectureCategory.builder()
                .lectureCategoryCode(lectureCategoryDTO.getLectureCategoryCode())
                .lectureCategoryName(LectureCategoryEnum.valueOf(lectureCategoryDTO.getLectureCategoryName()))
                .build();
    }
}
