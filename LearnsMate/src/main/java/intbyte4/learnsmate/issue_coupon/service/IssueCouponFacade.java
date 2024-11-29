package intbyte4.learnsmate.issue_coupon.service;

import intbyte4.learnsmate.coupon.domain.entity.CouponEntity;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.coupon_by_lecture.service.CouponByLectureService;
import intbyte4.learnsmate.issue_coupon.domain.IssueCoupon;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssueCouponDTO;
import intbyte4.learnsmate.issue_coupon.domain.dto.IssuedCouponFilterDTO;
import intbyte4.learnsmate.issue_coupon.domain.pagination.IssueCouponPageResponse;
import intbyte4.learnsmate.issue_coupon.domain.vo.request.IssueCouponRegisterRequestVO;
import intbyte4.learnsmate.issue_coupon.domain.vo.response.AllIssuedCouponResponseVO;
import intbyte4.learnsmate.issue_coupon.mapper.IssueCouponMapper;
import intbyte4.learnsmate.issue_coupon.repository.IssueCouponRepository;
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
public class IssueCouponFacade {

    private final MemberService memberService;
    private final IssueCouponService issueCouponService;
    private final MemberMapper memberMapper;
    private final CouponService couponService;
    private final CouponByLectureService couponByLectureService;
    private final IssueCouponRepository issueCouponRepository;
    private final IssueCouponMapper issueCouponMapper;

    @Transactional
    public List<IssueCouponDTO> issueCouponsToStudents(IssueCouponRegisterRequestVO request) {
        List<IssueCouponDTO> issuedCoupons = new ArrayList<>();

        for (Long studentCode : request.getStudentCodes()) {
            MemberDTO studentDTO = memberService.findByStudentCode(studentCode);
            Member student = memberMapper.fromMemberDTOtoMember(studentDTO);
            issuedCoupons.addAll(issueCouponsToStudent(student, request.getCouponCodes()));
        }

        return issuedCoupons;
    }

    private List<IssueCouponDTO> issueCouponsToStudent(Member student, List<Long> couponCodes) {
        List<IssueCouponDTO> issuedCoupons = new ArrayList<>();

        for (Long couponCode : couponCodes) {
            IssueCouponDTO issueCoupon = issueCouponService.createAndSaveIssueCoupon(student, couponCode);
            issuedCoupons.add(issueCoupon);
        }
        return issuedCoupons;
    }

    public List<AllIssuedCouponResponseVO> findAllIssuedCoupons() {
        List<IssueCouponDTO> issuedCouponDTOList = issueCouponService.findAllIssuedCoupons();
        List<AllIssuedCouponResponseVO> allIssuedCouponResponseVOList = new ArrayList<>();
        for (IssueCouponDTO issueCouponDTO : issuedCouponDTOList) {
            CouponEntity coupon = couponService.findByCouponCode(issueCouponDTO.getCouponCode());
            List<String> lectureCodes = couponByLectureService.getLectureCodesByCouponCode(coupon.getCouponCode());
            List<String> lectureNames = couponByLectureService.getLectureNamesByCouponCode(coupon.getCouponCode());
            List<String> tutorNames = couponByLectureService.getTutorNamesByCouponCode(coupon.getCouponCode());
            List<Integer> lecturePrices = couponByLectureService.getLecturePricesByCouponCode(coupon.getCouponCode());

            MemberDTO student = memberService.findByStudentCode(issueCouponDTO.getStudentCode());
            String studentName = student.getMemberName();

            AllIssuedCouponResponseVO response = AllIssuedCouponResponseVO.builder()
                    .couponIssuanceCode(issueCouponDTO.getCouponIssuanceCode())
                    .couponName(coupon.getCouponName())
                    .couponContents(coupon.getCouponContents())
                    .couponCategoryName(coupon.getCouponCategory().getCouponCategoryName())
                    .studentCode(issueCouponDTO.getStudentCode())
                    .studentName(studentName)
                    .couponDiscountRate(coupon.getCouponDiscountRate())
                    .couponUseStatus(issueCouponDTO.getCouponUseStatus())
                    .couponUseDate(issueCouponDTO.getCouponUseDate())
                    .couponIssueDate(issueCouponDTO.getCouponIssueDate())
                    .couponStartDate(coupon.getCouponStartDate())
                    .couponExpireDate(coupon.getCouponExpireDate())
                    .couponCode(issueCouponDTO.getCouponCode())
                    .lectureCode(lectureCodes)
                    .lectureName(lectureNames)
                    .tutorName(tutorNames)
                    .lecturePrice(lecturePrices)
                    .build();

            allIssuedCouponResponseVOList.add(response);
        }

        return allIssuedCouponResponseVOList;
    }

    public List<AllIssuedCouponResponseVO> filterIssuedCoupon(IssuedCouponFilterDTO dto) {
        log.info(dto.toString());
        // 필터링된 IssueCoupon 가져오기
        List<IssueCoupon> filteredIssuedCoupons = issueCouponService.getFilteredIssuedCoupons(dto);

        return filteredIssuedCoupons.stream().map(issueCoupon -> {
            CouponEntity coupon = issueCoupon.getCoupon();

            List<String> lectureCodes = couponByLectureService.getLectureCodesByCouponCode(coupon.getCouponCode());
            List<String> lectureNames = couponByLectureService.getLectureNamesByCouponCode(coupon.getCouponCode());
            List<String> tutorNames = couponByLectureService.getTutorNamesByCouponCode(coupon.getCouponCode());
            List<Integer> lecturePrices = couponByLectureService.getLecturePricesByCouponCode(coupon.getCouponCode());

            Long studentCode = issueCoupon.getStudent().getMemberCode();
            String studentName = issueCoupon.getStudent().getMemberName();


            AllIssuedCouponResponseVO responseVO = AllIssuedCouponResponseVO.builder()
                    .couponIssuanceCode(issueCoupon.getCouponIssuanceCode())
                    .couponName(coupon.getCouponName())
                    .couponContents(coupon.getCouponContents())
                    .couponCategoryName(coupon.getCouponCategory().getCouponCategoryName())
                    .studentCode(studentCode)
                    .studentName(studentName)
                    .couponDiscountRate(coupon.getCouponDiscountRate())
                    .couponUseStatus(issueCoupon.getCouponUseStatus())
                    .couponUseDate(issueCoupon.getCouponUseDate())
                    .couponIssueDate(issueCoupon.getCouponIssueDate())
                    .couponStartDate(coupon.getCouponStartDate())
                    .couponExpireDate(coupon.getCouponExpireDate())
                    .couponCode(coupon.getCouponCode())
                    .lectureCode(lectureCodes)
                    .lectureName(lectureNames)
                    .tutorName(tutorNames)
                    .lecturePrice(lecturePrices)
                    .build();

            log.info(filteredIssuedCoupons.toString());
            return responseVO;

        }).collect(Collectors.toList());
    }

    // offset 방식 전체 조회
    public IssueCouponPageResponse<AllIssuedCouponResponseVO> findAllIssuedCoupons(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<IssueCoupon> issueCouponPage = issueCouponRepository.findAllByOrderByCouponIssueDateDesc(pageable);

        List<AllIssuedCouponResponseVO> responseList = issueCouponPage.getContent().stream()
                .map(issueCoupon -> {
                    CouponEntity coupon = couponService.findByCouponCode(issueCoupon.getCoupon().getCouponCode());
                    List<String> lectureCodes = couponByLectureService.getLectureCodesByCouponCode(coupon.getCouponCode());
                    List<String> lectureNames = couponByLectureService.getLectureNamesByCouponCode(coupon.getCouponCode());
                    List<String> tutorNames = couponByLectureService.getTutorNamesByCouponCode(coupon.getCouponCode());
                    List<Integer> lecturePrices = couponByLectureService.getLecturePricesByCouponCode(coupon.getCouponCode());

                    MemberDTO student = memberService.findByStudentCode(issueCoupon.getStudent().getMemberCode());

                    return AllIssuedCouponResponseVO.builder()
                            .couponIssuanceCode(issueCoupon.getCouponIssuanceCode())
                            .couponName(coupon.getCouponName())
                            .couponContents(coupon.getCouponContents())
                            .couponCategoryName(coupon.getCouponCategory().getCouponCategoryName())
                            .studentCode(issueCoupon.getStudent().getMemberCode())
                            .studentName(student.getMemberName())
                            .couponDiscountRate(coupon.getCouponDiscountRate())
                            .couponUseStatus(issueCoupon.getCouponUseStatus())
                            .couponUseDate(issueCoupon.getCouponUseDate())
                            .couponIssueDate(issueCoupon.getCouponIssueDate())
                            .couponStartDate(coupon.getCouponStartDate())
                            .couponExpireDate(coupon.getCouponExpireDate())
                            .couponCode(issueCoupon.getCoupon().getCouponCode())
                            .lectureCode(lectureCodes)
                            .lectureName(lectureNames)
                            .tutorName(tutorNames)
                            .lecturePrice(lecturePrices)
                            .build();
                })
                .collect(Collectors.toList());

        return new IssueCouponPageResponse<>(
                responseList,
                issueCouponPage.getTotalElements(),
                issueCouponPage.getNumber(),
                issueCouponPage.getSize(),
                issueCouponPage.getTotalPages()
        );
    }

    // offset 방식 필터링
    public IssueCouponPageResponse<AllIssuedCouponResponseVO> filterIssuedCoupon(int page, int size, IssuedCouponFilterDTO dto) {
        log.info(dto.toString());
        PageRequest pageable = PageRequest.of(page, size);

        // dto -> IssueCouponFilterRequestVO 로 변환 후 필터링된 IssueCoupon 가져오기 (페이지네이션 적용)
        Page<IssueCoupon> issueCouponPage = issueCouponRepository.getFilteredIssuedCoupons(issueCouponMapper.fromDTOToFilterVO(dto), pageable);

        // DTO 리스트로 변환
        List<AllIssuedCouponResponseVO> responseList = issueCouponPage.getContent().stream()
                .map(issueCoupon -> {
                    CouponEntity coupon = issueCoupon.getCoupon();

                    List<String> lectureCodes = couponByLectureService.getLectureCodesByCouponCode(coupon.getCouponCode());
                    List<String> lectureNames = couponByLectureService.getLectureNamesByCouponCode(coupon.getCouponCode());
                    List<String> tutorNames = couponByLectureService.getTutorNamesByCouponCode(coupon.getCouponCode());
                    List<Integer> lecturePrices = couponByLectureService.getLecturePricesByCouponCode(coupon.getCouponCode());

                    Long studentCode = issueCoupon.getStudent().getMemberCode();
                    String studentName = issueCoupon.getStudent().getMemberName();

                    return AllIssuedCouponResponseVO.builder()
                            .couponIssuanceCode(issueCoupon.getCouponIssuanceCode())
                            .couponName(coupon.getCouponName())
                            .couponContents(coupon.getCouponContents())
                            .couponCategoryName(coupon.getCouponCategory().getCouponCategoryName())
                            .studentCode(studentCode)
                            .studentName(studentName)
                            .couponDiscountRate(coupon.getCouponDiscountRate())
                            .couponUseStatus(issueCoupon.getCouponUseStatus())
                            .couponUseDate(issueCoupon.getCouponUseDate())
                            .couponIssueDate(issueCoupon.getCouponIssueDate())
                            .couponStartDate(coupon.getCouponStartDate())
                            .couponExpireDate(coupon.getCouponExpireDate())
                            .couponCode(coupon.getCouponCode())
                            .lectureCode(lectureCodes)
                            .lectureName(lectureNames)
                            .tutorName(tutorNames)
                            .lecturePrice(lecturePrices)
                            .build();
                })
                .collect(Collectors.toList());

        return new IssueCouponPageResponse<>(
                responseList,               // 데이터 리스트
                issueCouponPage.getTotalElements(), // 전체 데이터 수
                issueCouponPage.getTotalPages(),    // 전체 페이지 수
                issueCouponPage.getNumber() + 1,    // 현재 페이지 (0-based → 1-based)
                issueCouponPage.getSize()           // 페이지 크기
        );
    }
}

