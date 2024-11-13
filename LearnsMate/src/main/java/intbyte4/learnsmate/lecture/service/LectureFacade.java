package intbyte4.learnsmate.lecture.service;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.mapper.CouponMapper;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.coupon_by_lecture.domain.dto.CouponByLectureDTO;
import intbyte4.learnsmate.coupon_by_lecture.service.CouponByLectureService;
import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.coupon_category.service.CouponCategoryServiceImpl;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.dto.LectureDetailDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.repository.LectureRepository;
import intbyte4.learnsmate.lecture_by_student.domain.entity.LectureByStudent;
import intbyte4.learnsmate.lecture_by_student.repository.LectureByStudentRepository;
import intbyte4.learnsmate.lecture_by_student.service.LectureByStudentService;
import intbyte4.learnsmate.lecture_category_by_lecture.domain.dto.OneLectureCategoryListDTO;
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
    private final CouponByLectureService couponByLectureService;
    private final CouponMapper couponMapper;
    private final CouponCategoryServiceImpl couponCategoryService;
    private final AdminService adminService;
    private final AdminMapper adminMapper;

    @Transactional
    public LectureDTO discountLecturePrice(LectureDTO lectureDTO, IssueCouponDTO issueCouponDTO) {
        MemberDTO tutorDTO = memberService.findById(lectureDTO.getTutorCode());
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);

        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor);

        MemberDTO studentDTO = memberService.findById(issueCouponDTO.getStudentCode());
        Member student = memberMapper.fromMemberDTOtoMember(studentDTO);

        CouponDTO couponDTO = couponService.findCouponDTOByCouponCode(issueCouponDTO.getCouponCode());
        // 쿠폰카테고리디티오로 반환하는게 필요
        CouponCategory couponCategory = couponCategoryService.findByCouponCategoryCode(couponDTO.getCouponCategoryCode());

        AdminDTO adminDTO = adminService.findByAdminCode(couponDTO.getAdminCode());
        Admin admin = adminMapper.toEntity(adminDTO);
        CouponEntity coupon = couponMapper.toEntity(couponDTO,couponCategory,admin,tutor);

        CouponByLectureDTO couponByLectureDTO = couponByLectureService.findByCouponAndLecture(lecture, coupon);
        if(couponByLectureDTO == null) return lectureDTO;
        lectureDTO.setLecturePrice(lectureDTO.getLecturePrice() * (1 - couponDTO.getCouponDiscountRate() / 100));
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


    @Transactional
    public LectureDTO registerLecture(LectureDTO lectureDTO, List<Integer> lectureCategoryCodeList, List<VideoByLectureDTO> videoByLectureDTOList) {

        // 강의를 담당하는 튜터 정보 조회 및 변환
        MemberDTO memberDTO = memberService.findMemberByMemberCode(lectureDTO.getTutorCode(), MemberType.TUTOR);
        Member tutor = memberMapper.fromMemberDTOtoMember(memberDTO);

        // 강의 엔티티 생성 및 저장
        Lecture lecture = Lecture.builder()
                .lectureTitle(lectureDTO.getLectureTitle())
                .lectureConfirmStatus(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lectureImage(lectureDTO.getLectureImage())
                .lecturePrice(lectureDTO.getLecturePrice())
                .tutor(tutor)
                .lectureStatus(true)
                .lectureClickCount(0)
                .lectureLevel(lectureDTO.getLectureLevel())
                .build();

        lectureRepository.save(lecture);

        // 강의별 카테고리 등록
        OneLectureCategoryListDTO oneLectureCategoryListDTO
                = new OneLectureCategoryListDTO(lecture.getLectureCode(), lectureCategoryCodeList);
        lectureCategoryByLectureService.saveLectureCategoryByLecture(oneLectureCategoryListDTO);

        Long lectureCode = lecture.getLectureCode();

        videoByLectureDTOList.forEach(videoDTO -> {
            videoByLectureService.registerVideoByLecture(lectureCode, videoDTO);
        });

        // 강의 정보 DTO로 변환하여 반환
        return lectureMapper.toDTO(lecture);
    }


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