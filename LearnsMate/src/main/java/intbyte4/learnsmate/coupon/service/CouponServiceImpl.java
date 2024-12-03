package intbyte4.learnsmate.coupon.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.domain.entity.CustomUserDetails;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.campaign.domain.dto.FindCampaignDetailDTO;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.coupon.domain.dto.ClientFindCouponDTO;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.domain.dto.CouponFilterDTO;
import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.domain.pagination.CouponPageResponse;
import intbyte4.learnsmate.coupon.domain.vo.response.CouponFindResponseVO;
import intbyte4.learnsmate.coupon.mapper.CouponMapper;
import intbyte4.learnsmate.coupon.repository.CouponRepository;
import intbyte4.learnsmate.coupon_by_lecture.service.CouponByLectureService;
import intbyte4.learnsmate.coupon_category.service.CouponCategoryService;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.member.domain.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("couponService")
@Slf4j
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;
    private final AdminService adminService;
    private final LectureService lectureService;
    private final CouponByLectureService couponByLectureService;
    private final LectureMapper lectureMapper;
    private final CouponCategoryService couponCategoryService;
    private final AdminMapper adminMapper;

    @Override
    public List<CouponDTO> findAllCoupons() {

        List<CouponEntity> couponEntities = couponRepository.findAllByCouponFlagTrue();
        List<CouponDTO> couponDTOList = new ArrayList<>();
        couponEntities.forEach(entity -> couponDTOList.add(couponMapper.toDTO(entity)));

        return couponDTOList;
    }

    @Override
    public CouponDTO findCouponDTOByCouponCode(Long couponCode) {
        CouponEntity couponEntity = couponRepository.findById(couponCode)
                .orElseThrow(() -> new CommonException(StatusEnum.COUPON_NOT_FOUND));
        return couponMapper.toDTO(couponEntity);
    }

    @Override
    public CouponEntity findByCouponCode(Long couponCode) {
        return couponRepository.findById(couponCode).orElseThrow(() -> new CommonException(StatusEnum.COUPON_NOT_FOUND));
    }

    @Override
    public Page<CouponDTO> findCouponsByCampaignCode(FindCampaignDetailDTO campaignDTO, Pageable pageable) {
        return couponRepository.findCouponsByCampaignCode(campaignDTO.getCampaignCode(), pageable)
                .map(couponMapper::toDTO);
    }

    @Transactional
    @Override
    public CouponDTO adminRegisterCoupon(CouponDTO requestCoupon
            , List<String> lectureCodeList) {

        Admin adminEntity = adminMapper.toEntity(adminService.findByAdminCode(getAdminCode()));
        log.info("{}", adminEntity.toString());
        CouponEntity newCoupon = CouponEntity.builder()
                .couponName(requestCoupon.getCouponName())
                .couponContents(requestCoupon.getCouponContents())
                .couponDiscountRate(requestCoupon.getCouponDiscountRate())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .couponStartDate(requestCoupon.getCouponStartDate())
                .couponExpireDate(requestCoupon.getCouponExpireDate())
                .couponFlag(true)
                .activeState(true)
                .couponCategory(couponCategoryService.findByCouponCategoryCode(requestCoupon.getCouponCategoryCode()))
                .admin(adminEntity)
                .tutor(null)
                .build();

        // coupon 저장
        CouponEntity savedCoupon = couponRepository.save(newCoupon);
        CouponDTO savedCouponDTO = couponMapper.toDTO(savedCoupon);

        lectureCodeList.forEach(lectureCode -> {
            LectureDTO selectedLectures = lectureService.getLectureById(lectureCode);
            Lecture lectures = lectureMapper.fromDTOToEntity(selectedLectures);

            if (lectures == null) throw new CommonException(StatusEnum.LECTURE_NOT_FOUND);

            couponByLectureService.registerCouponByLecture(lectures, newCoupon);
        });

        return savedCouponDTO;
    }

    @Override
    @Transactional
    public CouponDTO editAdminCoupon(CouponDTO couponDTO) {
        log.info("직원 쿠폰 수정 중: {}", couponDTO);
//        validAdmin(adminService, couponDTO.getAdminCode(), log);
        validAdmin(adminService, getAdminCode(), log);

        CouponEntity coupon = couponRepository.findById(couponDTO.getCouponCode())
                .orElseThrow(() -> new CommonException(StatusEnum.COUPON_NOT_FOUND));
        coupon.updateAdminCouponDetails(couponDTO);

        log.info("데이터베이스에 수정된 직원 쿠폰 저장 중: {}", coupon);
        CouponEntity updatedCoupon = couponRepository.save(coupon);
        log.info("수정된 직원 쿠폰 객체: {}", updatedCoupon);

        return couponMapper.fromEntityToDTO(updatedCoupon);
    }

    @Override
    @Transactional
    public CouponDTO editTutorCoupon(CouponDTO couponDTO) {
        log.info("강사 쿠폰 수정 중: {}", couponDTO);

        CouponEntity coupon = couponRepository.findById(couponDTO.getCouponCode())
                .orElseThrow(() -> new CommonException(StatusEnum.COUPON_NOT_FOUND));

        coupon.updateTutorCouponDetails(couponDTO);

        log.info("데이터베이스에 수정된 강사 쿠폰 저장 중: {}", coupon);
        CouponEntity updatedCoupon = couponRepository.save(coupon);
        log.info("수정된 강사 쿠폰 객체: {}", updatedCoupon);

        return couponMapper.fromEntityToDTO(updatedCoupon);
    }

    @Override
    @Transactional
    public CouponDTO deleteAdminCoupon(Long couponCode) {
        log.info("직원 쿠폰 삭제 중: couponCode = {}", couponCode);
        log.info("삭제한 직원 코드: adminCode = {}", getAdminCode());

        CouponEntity coupon = couponRepository.findById(couponCode)
                .orElseThrow(() -> new CommonException(StatusEnum.COUPON_NOT_FOUND));
        coupon.deleteCoupon();

        log.info("쿠폰 비활성화: {}", coupon);
        CouponEntity updatedCoupon = couponRepository.save(coupon);

        return couponMapper.fromEntityToDTO(updatedCoupon);
    }

    @Override
    @Transactional
    public CouponDTO tutorDeleteCoupon(Long couponCode) {
        log.info("강사 쿠폰 삭제 중: {}", couponCode);

//        validTutor(couponDTO, tutor);

        CouponEntity coupon = couponRepository.findById(couponCode).orElseThrow(() -> new CommonException(StatusEnum.COUPON_NOT_FOUND));
        coupon.deleteCoupon();

        log.info("강사 쿠폰 삭제: {}", coupon);
        CouponEntity updatedCoupon = couponRepository.save(coupon);

        return couponMapper.fromEntityToDTO(updatedCoupon);
    }

    @Override
    @Transactional
    public CouponDTO tutorInactiveCoupon(Long couponCode) {
        log.info("강사 쿠폰 비활성화 중: couponCode = {}", couponCode);

        CouponEntity coupon = couponRepository.findById(couponCode).orElseThrow(() -> new CommonException(StatusEnum.COUPON_NOT_FOUND));
        coupon.inactivateCoupon();

        log.info("강사 쿠폰 비활성화: {}", coupon);
        CouponEntity updatedCoupon = couponRepository.save(coupon);

        return couponMapper.fromEntityToDTO(updatedCoupon);
    }

    @Override
    @Transactional
    public CouponDTO tutorActivateCoupon(Long couponCode) {
        log.info("강사 쿠폰 활성화 중: {}", couponCode);

        CouponEntity coupon = couponRepository.findById(couponCode).orElseThrow(() -> new CommonException(StatusEnum.COUPON_NOT_FOUND));
        coupon.activateCoupon();

        log.info("강사 쿠폰 활성화: {}", coupon);
        CouponEntity updatedCoupon = couponRepository.save(coupon);

        return couponMapper.fromEntityToDTO(updatedCoupon);
    }

    @Override
    @Transactional
    public void saveCoupon(CouponEntity couponEntity) {
        couponRepository.save(couponEntity);
    }

    @Override
    public List<CouponEntity> filterCoupons(CouponFilterDTO dto) {
        return couponRepository.findCouponsByFilters(couponMapper.fromFilterDTOToFilterVO(dto));
    }

    // 필터링 offset pagination
    @Override
    public CouponPageResponse<CouponFindResponseVO> filterCoupons(CouponFilterDTO dto, int page, int size){

        log.info("{}", dto.toString());
        Pageable pageable = PageRequest.of(page, size);

        // 필터 조건과 페이징 처리된 데이터 조회
        Page<CouponEntity> couponPage = couponRepository.searchBy(couponMapper.fromFilterDTOToFilterVO(dto), pageable);

        // DTO 리스트로 변환
        List<CouponFindResponseVO> couponVOList = couponPage.getContent().stream()
                .map(couponMapper::fromCouponEntityToCouponFindResponseVO)
                .collect(Collectors.toList());

        log.info("{}", couponVOList);
        return new CouponPageResponse<>(
                couponVOList,               // 데이터 리스트
                couponPage.getTotalElements(), // 전체 데이터 수
                couponPage.getTotalPages(),    // 전체 페이지 수
                couponPage.getNumber() + 1,    // 현재 페이지 (0-based → 1-based)
                couponPage.getSize()           // 페이지 크기
        );
    }
    @Override
    public List<ClientFindCouponDTO> findAllClientCoupon(Long tutorCode) {
        List<ClientFindCouponDTO> dtoList = couponRepository.findAllClientCoupon(tutorCode);

        return dtoList;
    }
    public void validAdmin(AdminService adminService, Long adminCode, Logger log) {
        AdminDTO adminDTO = adminService.findByAdminCode(adminCode);
        if (adminDTO == null) {
            log.warn("존재하지 않는 직원 : {}", adminCode);
            throw new CommonException(StatusEnum.ADMIN_NOT_FOUND);
        }
        log.info(adminDTO.toString());
    }

    private static void validTutor(CouponDTO couponDTO, Member tutor) {
        if (!couponDTO.getTutorCode().equals(tutor.getMemberCode())) {
            log.warn("수정 권한 없음: {}", tutor);
            throw new CommonException(StatusEnum.RESTRICTED);
        }
        log.info(tutor.toString());
    }

    public Long getAdminCode() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new CommonException(StatusEnum.USER_NOT_FOUND);
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof String) {
            throw new CommonException(StatusEnum.USER_NOT_FOUND);
        }

        if (principal instanceof CustomUserDetails userDetails) {
            log.info("Authentication: {}", authentication);
            log.info("userDetails: {}", userDetails.toString());
            return userDetails.getUserDTO().getAdminCode();
        }

        throw new CommonException(StatusEnum.USER_NOT_FOUND);
    }
}
