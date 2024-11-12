package intbyte4.learnsmate.lecture.service;


import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.service.IssueCouponServiceImpl;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.repository.LectureRepository;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final LectureMapper lectureMapper;
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final IssueCouponServiceImpl issueCouponService;


    // 전체 강의 조회
    @Override
    public List<LectureDTO> getAllLecture() {
        List<Lecture> lectureList = lectureRepository.findAll();
        if (lectureList.isEmpty()) {
            throw new CommonException(StatusEnum.LECTURE_NOT_FOUND);
        }
        return lectureList.stream()
                .map(lectureMapper::toDTO)
                .collect(Collectors.toList());
    }


    // 강의 단건 조회
    @Override
    public LectureDTO getLectureById(Long lectureCode) {
        Lecture lecture = lectureRepository.findById(lectureCode)
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_NOT_FOUND));
        return lectureMapper.toDTO(lecture);
    }

    // 강사별 강의 모두 조회
    @Override
    public List<LectureDTO> getLecturesByTutorCode(Long tutorCode) {
        MemberDTO tutorDTO = memberService.findMemberByMemberCode(tutorCode, MemberType.TUTOR);
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);

        List<Lecture> lectures = lectureRepository.findAllByTutor(tutor);

        if (lectures.isEmpty()) {
            throw new CommonException(StatusEnum.LECTURE_NOT_FOUND);
        }

        return lectures.stream()
                .map(lectureMapper::toDTO)
                .collect(Collectors.toList());
    }


    // 카테고리별 강의 조회
//    public List<Lecture> filterLectures(LectureFilterDTO filter) {
//        Specification<Lecture> spec = LectureSpecifications.filterByCriteria(filter);
//        return lectureRepository.findAll(spec);
//    }


    // 강의 등록 -> 강의별 강의 카테고리 등록 메소드 가져오기 .
    @Override
    @Transactional
    public LectureDTO registerLecture(LectureDTO lectureDTO, List<Integer> lectureCategoryCodeList) {

//    lectureCategoryByLectureService의 등록 메서드 호출 필요
//        OneLectureCategoryListDTO oneLectureCategoryListDTO
//                = new OneLectureCategoryListDTO(lectureDTO.getLectureCode(), lectureCategoryCodeList);
//        lectureCategoryByLectureService.saveLectureCategoryByLecture(oneLectureCategoryListDTO);


        MemberDTO memberDTO = memberService.findMemberByMemberCode(lectureDTO.getTutorCode(), MemberType.TUTOR);
        Member member = memberMapper.fromMemberDTOtoMember(memberDTO);

        Lecture lecture = Lecture.builder()
                .lectureTitle(lectureDTO.getLectureTitle())
                .lectureConfirmStatus(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lectureImage(lectureDTO.getLectureImage())
                .lecturePrice(lectureDTO.getLecturePrice())
                .tutor(member)
                .lectureStatus(true)
                .lectureClickCount(0)
                .lectureLevel(lectureDTO.getLectureLevel())
                .build();

        lectureRepository.save(lecture);
        return lectureMapper.toDTO(lecture);
    }


    //강의별 계약과정이 강의 코드가 7개 라면 강의컬럼의 승인여부  true로 변환


    // 강의 수정
    @Override
    @Transactional
    public LectureDTO updateLecture(Long lectureCode, LectureDTO lectureDTO) {
        // LectureDTO에 lectureCode가 있으므로 파라미터를 저렇게 안넘겨줘도 될거 같아요.

        Lecture lecture = lectureRepository.findById(lectureCode)
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_NOT_FOUND));

//        LectureCategoryDTO lectureCategoryDTO =
//                lectureCategoryService.findByLectureCategoryCode(lectureDTO.getLectureCategoryCode());
//        LectureCategory lectureCategory = lectureCategoryMapper.toEntity(lectureCategoryDTO);
        // LectureCategory를 안쓰니까 위에꺼는 삭제해줘도 될거같아요.

//        lecture.toUpdate(lectureDTO, lectureCategory);
        lecture.toUpdate(lectureDTO, null);
        lectureRepository.save(lecture);
        return lectureMapper.toDTO(lecture);
    }


    // 강의 삭제
    @Override
    @Transactional
    public LectureDTO removeLecture(Long lectureCode) {
        Lecture lecture = lectureRepository.findById(lectureCode)
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_NOT_FOUND));
        lecture.toDelete();
        lectureRepository.save(lecture);
        return lectureMapper.toDTO(lecture);
    }

    @Override
    public LectureDTO discountLecture(LectureDTO lectureCode, IssueCouponDTO couponCode){
        MemberDTO memberDTO = memberService.findMemberByMemberCode(lectureCode.getTutorCode(), MemberType.TUTOR);
        Member member = memberMapper.fromMemberDTOtoMember(memberDTO);

        Lecture getLectureCode = lectureMapper.toEntity(lectureCode,member);
        Lecture lecture = lectureRepository.findById(getLectureCode.getLectureCode())
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_NOT_FOUND));

        LectureDTO lectureDTO = lectureMapper.toDTO(lecture);

        if (couponCode.getCouponCode() != null) {
            int discountRate = issueCouponService.


        }
        return null;
    }
}
