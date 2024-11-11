package intbyte4.learnsmate.lecture_category.mapper;

import intbyte4.learnsmate.lecture_category.domain.dto.LectureCategoryDTO;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategory;
import org.springframework.stereotype.Component;

@Component
public class LectureCategoryMapper {

    // 엔티티 -> DTO 변환
    public LectureCategoryDTO toDTO(LectureCategory lectureCategory) {
        return new LectureCategoryDTO(
                lectureCategory.getLectureCategoryCode(),
                lectureCategory.getLectureCategoryName()
        );
    }

    // DTO -> 엔티티 변환
    public LectureCategory toEntity(LectureCategoryDTO lectureCategoryDTO) {
        return new LectureCategory(
                lectureCategoryDTO.getLectureCategoryCode(),
                lectureCategoryDTO.getLectureCategoryName()
        );
    }
}
