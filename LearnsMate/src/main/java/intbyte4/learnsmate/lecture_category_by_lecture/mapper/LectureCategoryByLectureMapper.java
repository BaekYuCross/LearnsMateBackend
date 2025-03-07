package intbyte4.learnsmate.lecture_category_by_lecture.mapper;

import intbyte4.learnsmate.lecture_category_by_lecture.domain.dto.LectureCategoryByLectureDTO;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.entity.LectureCategoryByLecture;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.vo.response.ResponseFindLectureCategoryByLectureVO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LectureCategoryByLectureMapper {

    public List<LectureCategoryByLectureDTO> fromEntityToDTO(List<LectureCategoryByLecture> entityList) {
        return entityList.stream()
                .map(entity -> LectureCategoryByLectureDTO.builder()
                        .lectureCategoryByLectureCode(entity.getLectureCategoryByLectureCode())
                        .lectureCode(entity.getLecture().getLectureCode())
                        .lectureCategoryCode(entity.getLectureCategory().getLectureCategoryCode())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ResponseFindLectureCategoryByLectureVO> fromDTOtoFindVO(List<LectureCategoryByLectureDTO> dtoList) {
        return dtoList.stream()
                .map(dto -> ResponseFindLectureCategoryByLectureVO.builder()
                        .lectureCategoryByLectureCode(dto.getLectureCategoryByLectureCode())
                        .lectureCode(dto.getLectureCode())
                        .lectureCategoryCode(dto.getLectureCategoryCode())
                        .build())
                .collect(Collectors.toList());
    }

    public LectureCategoryByLectureDTO toDTO(LectureCategoryByLecture lectureCategoryByLecture) {
        return LectureCategoryByLectureDTO.builder()
                .lectureCategoryByLectureCode(lectureCategoryByLecture.getLectureCategoryByLectureCode())
                .lectureCategoryCode(lectureCategoryByLecture.getLectureCategory().getLectureCategoryCode())
                .lectureCode(lectureCategoryByLecture.getLecture().getLectureCode())
                .build();
    }
}
