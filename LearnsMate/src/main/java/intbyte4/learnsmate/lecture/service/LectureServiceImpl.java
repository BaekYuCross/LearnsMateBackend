package intbyte4.learnsmate.lecture.service;


import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.mapper.CouponMapper;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.coupon_by_lecture.service.CouponByLectureServiceImpl;
import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.mapper.IssueCouponMapper;
import intbyte4.learnsmate.issue_coupon.repository.IssueCouponRepository;
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
    public LectureDTO updateLecture(LectureDTO lectureDTO) {
        Lecture lecture = lectureRepository.findById(lectureDTO.getLectureCode())
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_NOT_FOUND));

        lecture.toUpdate(lectureDTO);
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

}
