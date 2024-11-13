package intbyte4.learnsmate.lecture_category_by_lecture.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
 import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.lecture_category.domain.dto.LectureCategoryDTO;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategory;
import intbyte4.learnsmate.lecture_category.mapper.LectureCategoryMapper;
import intbyte4.learnsmate.lecture_category.service.LectureCategoryService;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.dto.LectureCategoryByLectureDTO;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.dto.OneLectureCategoryListDTO;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.entity.LectureCategoryByLecture;
import intbyte4.learnsmate.lecture_category_by_lecture.mapper.LectureCategoryByLectureMapper;
import intbyte4.learnsmate.lecture_category_by_lecture.repository.LectureCategoryByLectureRepository;

import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;

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

    // 모든 강의별 강의 카테고리 조회 메서드
    public List<LectureCategoryByLectureDTO> findAll() {

        List<LectureCategoryByLecture> entityList = lectureCategoryByLectureRepository.findAll();

        return lectureCategoryByLectureMapper.fromEntitytoDTO(entityList);
    }

    // 특정 강의별 강의 카테고리 조회 메서드
    public LectureCategoryByLectureDTO findById(Long code) {
        LectureCategoryByLecture entity = lectureCategoryByLectureRepository.findById(code)
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_CATEGORY_BY_LECTURE_NOT_FOUND));

        return lectureCategoryByLectureMapper.fromEntitytoDTO(List.of(entity)).get(0);
    }

    // 특정 강의에 강의 카테고리 추가 메서드 -> 어떤 dto를 써야할까? OneLectureCategoryListDTO
    public void saveLectureCategoryByLecture(OneLectureCategoryListDTO dto){

        // 어떠한 강의
        LectureDTO lectureDTO = lectureService.getLectureById(dto.getLectureCode());

        // 강의 안에있는 멤버
        MemberDTO memberDTO = memberService.findById(lectureDTO.getTutorCode());
        Member tutor = memberMapper.fromMemberDTOtoMember(memberDTO);

        // 강의 완성
        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor);

        // 여러개의 강의 카테고리
        List<LectureCategory> lectureCategoryList = new ArrayList<>();
        for (int i = 0; i < dto.getLectureCategoryCodeList().size(); i++) {
            LectureCategoryDTO categoryDTO = lectureCategoryService.findByLectureCategoryCode(dto.getLectureCategoryCodeList().get(i));
            LectureCategory lectureCategory = lectureCategoryMapper.toEntity(categoryDTO);
            lectureCategoryList.add(lectureCategory);
        }

        // 등록할 강의별 강의 카테고리 완성
        List<LectureCategoryByLecture> entitytList
                = lectureCategoryByLectureMapper.fromLectureAndLectureCategoryListtoEntity(lecture, lectureCategoryList);

        // 강의별 강의 카테고리 테이블에 저장
        lectureCategoryByLectureRepository.saveAll(entitytList);
    }

    public List<String> findCategoryNamesByLectureCode(Long lectureCode) {
        return lectureCategoryByLectureRepository.findCategoryNamesByLectureCode(lectureCode);
    }

}
