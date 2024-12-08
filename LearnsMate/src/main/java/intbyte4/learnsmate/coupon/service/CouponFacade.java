package intbyte4.learnsmate.coupon.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.dto.CouponFilterDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.domain.pagination.CouponPageResponse;
import intbyte4.learnsmate.coupon.domain.vo.request.TutorCouponRegisterRequestVO;
import intbyte4.learnsmate.coupon.domain.vo.response.CouponFindResponseVO;
import intbyte4.learnsmate.coupon.mapper.CouponMapper;
import intbyte4.learnsmate.coupon.repository.CouponRepository;
import intbyte4.learnsmate.coupon_by_lecture.service.CouponByLectureService;
import intbyte4.learnsmate.coupon_category.domain.CouponCategory;
import intbyte4.learnsmate.coupon_category.service.CouponCategoryService;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponFacade {

    private final CouponCategoryService couponCategoryService;
    private final AdminService adminService;
    private final MemberService memberService;
    private final CouponService couponService;
    private final LectureService lectureService;
    private final CouponByLectureService couponByLectureService;
    private final CouponMapper couponMapper;
    private final LectureMapper lectureMapper;
    private final MemberMapper memberMapper;
    private final CouponRepository couponRepository;

    @Transactional
    public CouponDTO tutorRegisterCoupon(TutorCouponRegisterRequestVO request) {

        Long tutorCode = request.getTutorCode();
        Integer couponCategoryCode = request.getCouponCategoryCode();
        String lectureCode = request.getLectureCode();

        MemberDTO tutorDTO = memberService.findById(tutorCode);
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);

        CouponCategory couponCategory = couponCategoryService.findByCouponCategoryCode(couponCategoryCode);

        CouponEntity newCouponEntity = couponMapper.newCouponEntity(request, tutor, couponCategory);

        LectureDTO lectureDTO = lectureService.getLectureById(lectureCode);
        Lecture lectureEntity = lectureMapper.toEntity(lectureDTO, tutor);

        couponService.saveCoupon(newCouponEntity);
        couponByLectureService.registerCouponByLecture(lectureEntity, newCouponEntity);
        return couponMapper.toDTO(newCouponEntity);
    }

    // offset 페이지네이션을 위한 전체 쿠폰 조회 함수
    @Transactional
    public CouponPageResponse<CouponFindResponseVO> findAllCoupons(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<CouponEntity> couponPage = couponRepository.findAllByCoupon(pageable);

        List<CouponFindResponseVO> responseVOList = couponPage.getContent().stream()
                .map(couponMapper::fromCouponEntityToCouponFindResponseVO)
                .collect(Collectors.toList());

        return new CouponPageResponse<>(
                responseVOList,
                couponPage.getTotalElements(),
                couponPage.getTotalPages(),
                couponPage.getNumber(),
                couponPage.getSize()
        );
    }

    // offset 페이지네이션을 위한 전체 admin 쿠폰 조회
    public CouponPageResponse<CouponFindResponseVO> findAdminCoupons(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<CouponEntity> couponPage = couponRepository.findAllAdminCoupons(pageable);

        List<CouponFindResponseVO> responseVOList = couponPage.getContent().stream()
                .map(couponMapper::fromCouponEntityToCouponFindResponseVO)
                .collect(Collectors.toList());

        return new CouponPageResponse<>(
                responseVOList,
                couponPage.getTotalElements(),
                couponPage.getTotalPages(),
                couponPage.getNumber(),
                couponPage.getSize()
        );
    }

    // offset 페이지네이션을 위한 전체 tutor 쿠폰 조회
    public CouponPageResponse<CouponFindResponseVO> findTutorCoupons(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<CouponEntity> couponPage = couponRepository.findAllTutorCoupons(pageable);

        List<CouponFindResponseVO> responseVOList = couponPage.getContent().stream()
                .map(couponMapper::fromCouponEntityToCouponFindResponseVO)
                .collect(Collectors.toList());

        return new CouponPageResponse<>(
                responseVOList,
                couponPage.getTotalElements(),
                couponPage.getTotalPages(),
                couponPage.getNumber(),
                couponPage.getSize()
        );
    }

    @Transactional
    public List<CouponFindResponseVO> findAllCoupons() {
        List<CouponDTO> couponDTOList = couponService.findAllCoupons();
        List<CouponFindResponseVO> couponFindResponseVOList = new ArrayList<>();
        for (CouponDTO couponDTO : couponDTOList) {
            if (couponDTO.getAdminCode() != null) {
                AdminDTO adminDTO = adminService.findByAdminCode(couponDTO.getAdminCode());
                CouponCategory couponCategory = couponCategoryService.findByCouponCategoryCode(couponDTO.getCouponCategoryCode());
                CouponFindResponseVO response = CouponFindResponseVO.builder()
                        .couponCode(couponDTO.getCouponCode())
                        .couponName(couponDTO.getCouponName())
                        .couponContents(couponDTO.getCouponContents())
                        .couponDiscountRate(couponDTO.getCouponDiscountRate())
                        .couponCategoryName(couponCategory.getCouponCategoryName())
                        .couponCategoryCode(couponCategory.getCouponCategoryCode())
                        .activeState(couponDTO.getActiveState())
                        .couponStartDate(couponDTO.getCouponStartDate())
                        .couponExpireDate(couponDTO.getCouponExpireDate())
                        .createdAt(couponDTO.getCreatedAt())
                        .updatedAt(couponDTO.getUpdatedAt())
                        .adminCode(adminDTO.getAdminCode())
                        .adminName(adminDTO.getAdminName())
                        .tutorName(null)
                        .build();

                couponFindResponseVOList.add(response);
            } else {
                MemberDTO memberDTO = memberService.findById(couponDTO.getTutorCode());
                CouponCategory couponCategory = couponCategoryService.findByCouponCategoryCode(couponDTO.getCouponCategoryCode());
                CouponFindResponseVO response = CouponFindResponseVO.builder()
                        .couponCode(couponDTO.getCouponCode())
                        .couponName(couponDTO.getCouponName())
                        .couponContents(couponDTO.getCouponContents())
                        .couponDiscountRate(couponDTO.getCouponDiscountRate())
                        .couponCategoryName(couponCategory.getCouponCategoryName())
                        .activeState(couponDTO.getActiveState())
                        .couponStartDate(couponDTO.getCouponStartDate())
                        .couponExpireDate(couponDTO.getCouponExpireDate())
                        .createdAt(couponDTO.getCreatedAt())
                        .updatedAt(couponDTO.getUpdatedAt())
                        .adminName(null)
                        .tutorName(memberDTO.getMemberName())
                        .build();

                couponFindResponseVOList.add(response);
            }
        }
        return couponFindResponseVOList;
    }

    @Transactional
    public List<CouponFindResponseVO> findAdminRegisterCoupons () {
        List<CouponDTO> couponDTOList = couponService.findAllCoupons();
        List<CouponFindResponseVO> couponFindResponseVOList = new ArrayList<>();
        for (CouponDTO couponDTO : couponDTOList) {
            if (couponDTO.getAdminCode() != null) {
                AdminDTO adminDTO = adminService.findByAdminCode(couponDTO.getAdminCode());
                CouponCategory couponCategory = couponCategoryService.findByCouponCategoryCode(couponDTO.getCouponCategoryCode());
                CouponFindResponseVO response = CouponFindResponseVO.builder()
                        .couponCode(couponDTO.getCouponCode())
                        .couponName(couponDTO.getCouponName())
                        .couponContents(couponDTO.getCouponContents())
                        .couponDiscountRate(couponDTO.getCouponDiscountRate())
                        .couponCategoryCode(couponDTO.getCouponCategoryCode())
                        .couponCategoryName(couponCategory.getCouponCategoryName())
                        .activeState(couponDTO.getActiveState())
                        .couponStartDate(couponDTO.getCouponStartDate())
                        .couponExpireDate(couponDTO.getCouponExpireDate())
                        .createdAt(couponDTO.getCreatedAt())
                        .updatedAt(couponDTO.getUpdatedAt())
                        .adminCode(adminDTO.getAdminCode())
                        .adminName(adminDTO.getAdminName())
                        .tutorName(null)
                        .build();

                couponFindResponseVOList.add(response);
            }
        }
        return couponFindResponseVOList;
    }


    public List<CouponFindResponseVO> findTutorRegisterCoupons() {
        List<CouponDTO> couponDTOList = couponService.findAllCoupons();
        List<CouponFindResponseVO> couponFindResponseVOList = new ArrayList<>();
        for (CouponDTO couponDTO : couponDTOList) {
            if (couponDTO.getTutorCode() != null) {
                MemberDTO memberDTO = memberService.findById(couponDTO.getTutorCode());
                CouponCategory couponCategory = couponCategoryService.findByCouponCategoryCode(couponDTO.getCouponCategoryCode());
                CouponFindResponseVO response = CouponFindResponseVO.builder()
                        .couponCode(couponDTO.getCouponCode())
                        .couponName(couponDTO.getCouponName())
                        .couponContents(couponDTO.getCouponContents())
                        .couponDiscountRate(couponDTO.getCouponDiscountRate())
                        .couponCategoryName(couponCategory.getCouponCategoryName())
                        .activeState(couponDTO.getActiveState())
                        .couponStartDate(couponDTO.getCouponStartDate())
                        .couponExpireDate(couponDTO.getCouponExpireDate())
                        .createdAt(couponDTO.getCreatedAt())
                        .updatedAt(couponDTO.getUpdatedAt())
                        .adminName(null)
                        .tutorName(memberDTO.getMemberName())
                        .build();

                couponFindResponseVOList.add(response);
            }
        }
        return couponFindResponseVOList;
    }

    @Transactional
    public CouponFindResponseVO findCoupon(Long couponCode) {
        CouponEntity coupon = couponService.findByCouponCode(couponCode);
        if (coupon.getAdmin().getAdminCode() != null) {
            AdminDTO adminDTO = adminService.findByAdminCode(coupon.getAdmin().getAdminCode());
            CouponCategory couponCategory = couponCategoryService.findByCouponCategoryCode(coupon.getCouponCategory().getCouponCategoryCode());

            return CouponFindResponseVO.builder()
                    .couponCode(coupon.getCouponCode())
                    .couponName(coupon.getCouponName())
                    .couponContents(coupon.getCouponContents())
                    .couponDiscountRate(coupon.getCouponDiscountRate())
                    .couponCategoryName(couponCategory.getCouponCategoryName())
                    .activeState(coupon.getActiveState())
                    .couponStartDate(coupon.getCouponStartDate())
                    .couponExpireDate(coupon.getCouponExpireDate())
                    .createdAt(coupon.getCreatedAt())
                    .updatedAt(coupon.getUpdatedAt())
                    .adminName(adminDTO.getAdminName())
                    .tutorName(null)
                    .build();

        } else {
            MemberDTO memberDTO = memberService.findById(coupon.getTutor().getMemberCode());
            CouponCategory couponCategory = couponCategoryService.findByCouponCategoryCode(coupon.getCouponCategory().getCouponCategoryCode());
            CouponFindResponseVO response = CouponFindResponseVO.builder()
                    .couponCode(coupon.getCouponCode())
                    .couponName(coupon.getCouponName())
                    .couponContents(coupon.getCouponContents())
                    .couponDiscountRate(coupon.getCouponDiscountRate())
                    .couponCategoryName(couponCategory.getCouponCategoryName())
                    .activeState(coupon.getActiveState())
                    .couponStartDate(coupon.getCouponStartDate())
                    .couponExpireDate(coupon.getCouponExpireDate())
                    .createdAt(coupon.getCreatedAt())
                    .updatedAt(coupon.getUpdatedAt())
                    .adminName(null)
                    .tutorName(memberDTO.getMemberName())
                    .build();

            return response;
        }
    }

    @Transactional
    public List<CouponFindResponseVO> filterCoupon(CouponFilterDTO dto) {
        // 1. Repository에서 필터링된 쿠폰 목록 조회
        List<CouponEntity> filteredCoupons = couponService.filterCoupons(dto);

        // 2. 각 쿠폰에 대해 필요한 데이터 가져오기 (AdminName, CouponCategoryName, TutorName)
        return filteredCoupons.stream().map(coupon -> {
            String adminName = coupon.getAdmin() != null ? coupon.getAdmin().getAdminName() : null;
            String tutorName = coupon.getTutor() != null ? coupon.getTutor().getMemberName() : null;
            String couponCategoryName = coupon.getCouponCategory() != null ? coupon.getCouponCategory().getCouponCategoryName() : null;

            // 3. CouponFindResponseVO 객체로 매핑
            return CouponFindResponseVO.builder()
                    .couponCode(coupon.getCouponCode())
                    .couponName(coupon.getCouponName())
                    .couponContents(coupon.getCouponContents())
                    .couponDiscountRate(coupon.getCouponDiscountRate())
                    .couponCategoryName(couponCategoryName)
                    .activeState(coupon.getActiveState())
                    .couponStartDate(coupon.getCouponStartDate())
                    .couponExpireDate(coupon.getCouponExpireDate())
                    .createdAt(coupon.getCreatedAt())
                    .updatedAt(coupon.getUpdatedAt())
                    .adminName(adminName)
                    .tutorName(tutorName)
                    .build();
        }).collect(Collectors.toList());
    }

}



