package intbyte4.learnsmate.lecture_category_by_lecture.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
 import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.lecture_category.domain.dto.LectureCategoryDTO;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategory;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategoryEnum;
import intbyte4.learnsmate.lecture_category.mapper.LectureCategoryMapper;
import intbyte4.learnsmate.lecture_category.service.LectureCategoryService;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.dto.LectureCategoryByLectureDTO;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.entity.LectureCategoryByLecture;
import intbyte4.learnsmate.lecture_category_by_lecture.mapper.LectureCategoryByLectureMapper;
import intbyte4.learnsmate.lecture_category_by_lecture.repository.LectureCategoryByLectureRepository;

import intbyte4.learnsmate.member.domain.dto.CategoryCountDTO;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LectureCategoryByLectureService {

    private final LectureCategoryByLectureRepository lectureCategoryByLectureRepository;
    private final LectureCategoryByLectureMapper lectureCategoryByLectureMapper;

    private final LectureService lectureService;
    private final LectureMapper lectureMapper;
    private final MemberMapper memberMapper;
    private final MemberService memberService;
    private final LectureCategoryService lectureCategoryService;
    private final LectureCategoryMapper lectureCategoryMapper;

    public List<LectureCategoryByLectureDTO> findAll() {

        List<LectureCategoryByLecture> entityList = lectureCategoryByLectureRepository.findAll();

        return lectureCategoryByLectureMapper.fromEntityToDTO(entityList);
    }

    public LectureCategoryByLectureDTO findById(Long code) {
        LectureCategoryByLecture entity = lectureCategoryByLectureRepository.findById(code)
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_CATEGORY_BY_LECTURE_NOT_FOUND));

        return lectureCategoryByLectureMapper.fromEntityToDTO(List.of(entity)).get(0);
    }

    public void saveLectureCategoryByLecture(String lectureCode, Integer lectureCategoryCode){
        LectureDTO lectureDTO = lectureService.getLectureById(lectureCode);
        MemberDTO memberDTO = memberService.findById(lectureDTO.getTutorCode());
        Member tutor = memberMapper.fromMemberDTOtoMember(memberDTO);

        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor);

        LectureCategoryDTO lectureCategoryDTO = lectureCategoryService.findByLectureCategoryCode(lectureCategoryCode);
        LectureCategory lectureCategory = lectureCategoryMapper.toEntity(lectureCategoryDTO);

        LectureCategoryByLecture lectureCategoryByLecture = LectureCategoryByLecture.builder()
                .lecture(lecture)
                .lectureCategory(lectureCategory)
                .build();

        lectureCategoryByLectureRepository.save(lectureCategoryByLecture);
    }

    public List<String> findCategoryNamesByLectureCode(String lectureCode) {
        return lectureCategoryByLectureRepository.findCategoryNamesByLectureCode(lectureCode);
    }

    @Transactional
    public void deleteByLectureCode(String lectureCode) {
        lectureCategoryByLectureRepository.deleteAllByLectureCode(lectureCode);
    }

    public String findLectureCategoryNameByLectureCode(String lectureCode) {
        LectureCategoryByLectureDTO dto = lectureCategoryByLectureRepository.findLectureCategoryDetailsByLectureCode(lectureCode);
        LectureCategoryEnum categoryName = lectureCategoryService.findLectureCategoryNameByCode(dto.getLectureCategoryCode());
        return categoryName.name();
    }

    public List<CategoryCountDTO> countLecturesByCategory() {
        return lectureCategoryByLectureRepository.countLecturesByCategory();
    }

    public List<CategoryCountDTO> countLecturesByCategoryWithinDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return lectureCategoryByLectureRepository.countLecturesByCategoryWithinDateRange(startDate, endDate);
    }

    public LectureCategoryByLectureDTO findLectureCategoryByLectureCode(String lectureCode) {
        LectureCategoryByLecture lectureCategoryByLecture = lectureCategoryByLectureRepository.findByLecture_LectureCode(lectureCode);
        return lectureCategoryByLectureMapper.toDTO(lectureCategoryByLecture);
    }
}
