package intbyte4.learnsmate.facade;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.dto.LectureDetailDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.repository.LectureRepository;
import intbyte4.learnsmate.lecture_by_student.domain.entity.LectureByStudent;
import intbyte4.learnsmate.lecture_by_student.repository.LectureByStudentRepository;
import intbyte4.learnsmate.lecture_by_student.service.LectureByStudentService;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.lecture_category_by_lecture.service.LectureCategoryByLectureService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.video_by_lecture.domain.dto.VideoByLectureDTO;
import intbyte4.learnsmate.video_by_lecture.service.VideoByLectureService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureFacade {
    private final LectureRepository lectureRepository;
    private final LectureMapper lectureMapper;
    private final CouponService couponService;
    private final LectureByStudentService lectureByStudentService;
    private final LectureByStudentRepository lectureByStudentRepository;
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final VideoByLectureService videoByLectureService;
    private final LectureService lectureService;
    private final LectureCategoryByLectureService lectureCategoryByLectureService;

    @Transactional
    public LectureDTO discountLecturePrice(LectureDTO lectureDTO, IssueCouponDTO issueCouponDTO) {
        if (issueCouponDTO == null) {
            return lectureDTO;
        }
        CouponEntity coupon = couponService.findByCouponCode(issueCouponDTO.getCouponCode());
        lectureDTO.setLecturePrice(lectureDTO.getLecturePrice() * (1 - coupon.getCouponDiscountRate() / 100));
        return lectureDTO;
    }

    @Transactional
    public LectureDTO removeLecture(Long lectureCode) {
        Lecture lecture = lectureRepository.findById(lectureCode)
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_NOT_FOUND));
        lecture.toDelete();
        lectureRepository.save(lecture);

        updateOwnStatus(lecture);
        return lectureMapper.toDTO(lecture);
    }

    @Transactional
    public void updateOwnStatus(Lecture lecture) {
        List<Long> lectureByStudentCodes = lectureByStudentRepository.findLectureByStudentCodesByLectureCode(lecture.getLectureCode());
        List<LectureByStudent> lectureByStudents = lectureByStudentRepository.findAllById(lectureByStudentCodes);
        for (LectureByStudent lectureByStudent : lectureByStudents) {
            lectureByStudent.changeOwnStatus();
        }

        lectureByStudentRepository.saveAll(lectureByStudents);
    }

    // 강사별 강의 모두 조회
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


    // 강의 등록 -> 강의별 강의 카테고리 등록 메소드 가져오기 .
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


    // 강의 모두 조회
    public List<LectureDetailDTO> getAllLecture() {
        List<LectureDTO> lectureList = lectureService.getAllLecture();

        if (lectureList.isEmpty()) {
            throw new CommonException(StatusEnum.LECTURE_NOT_FOUND);
        }

        return lectureList.stream()
                .map(this::buildLectureDetailDTO)
                .collect(Collectors.toList());
    }

    // 강의 단건 조회
    public LectureDetailDTO getLectureById(Long lectureCode) {
        LectureDTO lecture = lectureService.getLectureById(lectureCode);
        if (lecture == null) {
            throw new CommonException(StatusEnum.LECTURE_NOT_FOUND);
        }
        return buildLectureDetailDTO(lecture);
    }

    private LectureDetailDTO buildLectureDetailDTO(LectureDTO lecture) {
        MemberDTO tutor = memberService.findMemberByMemberCode(lecture.getTutorCode(), MemberType.TUTOR);
        long totalStudents = lectureByStudentService.countStudentsByLectureAndOwnStatus(lecture.getLectureCode());
        int totalRevenue = lectureByStudentService.calculateTotalRevenue(lecture.getLectureCode());
        List<VideoByLectureDTO> lectureVideos = videoByLectureService.findVideoByLectureByLectureCode(lecture.getLectureCode());
        List<String> lectureCategories = lectureCategoryByLectureService.findCategoryNamesByLectureCode(lecture.getLectureCode());

        return LectureDetailDTO.builder()
                .lectureCode(lecture.getLectureCode())
                .lectureTitle(lecture.getLectureTitle())
                .lectureConfirmStatus(lecture.getLectureConfirmStatus())
                .createdAt(lecture.getCreatedAt())
                .lectureImage(lecture.getLectureImage())
                .lecturePrice(lecture.getLecturePrice())
                .tutorCode(tutor.getMemberCode())
                .tutorName(tutor.getMemberName())
                .lectureStatus(lecture.getLectureStatus())
                .lectureCategory(String.join(", ", lectureCategories))
                .lectureClickCount(lecture.getLectureClickCount())
                .lectureLevel(lecture.getLectureLevel())
                .totalStudents((int) totalStudents)
                .totalRevenue(totalRevenue)
                .lectureVideos(lectureVideos)
                .build();
    }
}