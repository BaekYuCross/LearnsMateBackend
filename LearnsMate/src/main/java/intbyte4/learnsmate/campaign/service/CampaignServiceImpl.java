package intbyte4.learnsmate.campaign.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.campaign.domain.dto.CampaignDTO;
import intbyte4.learnsmate.campaign.domain.dto.CampaignFilterDTO;
import intbyte4.learnsmate.campaign.domain.dto.CampaignPageResponse;
import intbyte4.learnsmate.campaign.domain.dto.FindAllCampaignDTO;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.campaign.domain.entity.CampaignTypeEnum;
import intbyte4.learnsmate.campaign.domain.vo.response.ResponseFindCampaignByConditionVO;
import intbyte4.learnsmate.campaign.mapper.CampaignMapper;
import intbyte4.learnsmate.campaign.repository.CampaignRepository;

import intbyte4.learnsmate.campaign.repository.CampaignRepositoryCustom;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.couponbycampaign.domain.dto.CouponByCampaignDTO;
import intbyte4.learnsmate.couponbycampaign.service.CouponByCampaignService;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.userpercampaign.domain.dto.UserPerCampaignDTO;
import intbyte4.learnsmate.userpercampaign.service.UserPerCampaignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository campaignRepository;
    private final CampaignRepositoryCustom campaignRepositoryCustom;
    private final AdminService adminService;
    private final UserPerCampaignService userPerCampaignService;
    private final CouponByCampaignService couponByCampaignService;
    private final MemberService memberService;
    private final CouponService couponService;
    private final CampaignMapper campaignMapper;
    private final AdminMapper adminMapper;

    @Override
    public CampaignDTO registerCampaign(CampaignDTO requestCampaign
            , List<MemberDTO> requestStudentList
            , List<CouponDTO> requestCouponList) {

        LocalDateTime sendTime;
        AdminDTO adminDTO = adminService.findByAdminCode(requestCampaign.getAdminCode());
        Admin admin = adminMapper.toEntity(adminDTO);
        if (Objects.equals(requestCampaign.getCampaignType(), CampaignTypeEnum.INSTANT.getType())) {
            sendTime = LocalDateTime.now();
        } else {
            sendTime = requestCampaign.getCampaignSendDate();
            log.info("예약 시간 {}", sendTime);
        };

        Campaign campaign = Campaign.builder()
                .campaignCode(null)
                .campaignTitle(requestCampaign.getCampaignTitle())
                .campaignContents(requestCampaign.getCampaignContents())
                .campaignType(CampaignTypeEnum.valueOf(requestCampaign.getCampaignType()))
                .campaignSendDate(sendTime)
                .createdAt(requestCampaign.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .admin(admin)
                .build();

        Campaign savedCampaign = campaignRepository.save(campaign);
        CampaignDTO savedCampaignDTO = campaignMapper.toDTO(savedCampaign);

        requestStudentList.forEach(memberDTO -> {
            MemberDTO foundStudent = memberService.findMemberByMemberCode(memberDTO.getMemberCode()
                    , memberDTO.getMemberType());
            if (foundStudent == null) throw new CommonException(StatusEnum.STUDENT_NOT_FOUND);
            userPerCampaignService.registerUserPerCampaign(foundStudent, savedCampaignDTO);
        });

        requestCouponList.forEach(couponDTO -> {
            CouponDTO foundCoupon = couponService.findCouponDTOByCouponCode(couponDTO.getCouponCode());
            if (foundCoupon == null) throw new CommonException(StatusEnum.COUPON_NOT_FOUND);
            else if (foundCoupon.getTutorCode() != null) {
                throw new CommonException(StatusEnum.COUPON_CANNOT_BE_SENT_BY_TUTOR);
            }
            couponByCampaignService.registerCouponByCampaign(foundCoupon, savedCampaignDTO);
        });

        return campaignMapper.toDTO(campaign);
    }

    @Override
    public CampaignDTO editCampaign(CampaignDTO requestCampaign
            , List<MemberDTO> requestStudentList
            , List<CouponDTO> requestCouponList) {
        if (requestCampaign.getCampaignCode() == null) {
            throw new CommonException(StatusEnum.CAMPAIGN_NOT_FOUND);
        }
        if (Objects.equals(requestCampaign.getCampaignType(), CampaignTypeEnum.INSTANT.getType())) {
            throw new CommonException(StatusEnum.UPDATE_NOT_ALLOWED);
        }

        AdminDTO adminDTO = adminService.findByAdminCode(requestCampaign.getAdminCode());
        Admin admin = adminMapper.toEntity(adminDTO);

        Campaign updatedCampaign = campaignMapper.toEntity(requestCampaign, admin);

        campaignRepository.save(updatedCampaign);

        editStudent(requestCampaign, requestStudentList, updatedCampaign);

        editCoupon(requestCampaign, requestCouponList, updatedCampaign);

        return campaignMapper.toDTO(updatedCampaign);
    }

    private void editCoupon(CampaignDTO requestCampaign
            , List<CouponDTO> requestCouponList
            , Campaign updatedCampaign) {
        List<CouponByCampaignDTO> existingCouponList = couponByCampaignService
                .findByCampaignCode(updatedCampaign);

        List<CouponByCampaignDTO> couponsToRemove = existingCouponList.stream()
                .filter(coupon -> requestCouponList.stream()
                        .noneMatch(newCoupon -> newCoupon.getCouponCode().equals(coupon.getCouponCode())))
                .toList();

        List<CouponDTO> couponsToAdd = requestCouponList.stream()
                .filter(newCoupon -> existingCouponList.stream()
                        .noneMatch(coupon -> coupon.getCouponCode().equals(newCoupon.getCouponCode())))
                .toList();

        couponsToRemove.forEach(coupon -> couponByCampaignService
                .removeCouponByCampaign(coupon.getCouponByCampaignCode()));

        couponsToAdd.forEach(couponDTO -> {
            CouponDTO newCoupon = couponService.findCouponDTOByCouponCode(couponDTO.getCouponCode());
            if (newCoupon == null) throw new CommonException(StatusEnum.COUPON_NOT_FOUND);
            couponByCampaignService.registerCouponByCampaign(newCoupon, requestCampaign);
        });
    }

    private void editStudent(CampaignDTO requestCampaign
            , List<MemberDTO> requestStudentList
            , Campaign updatedCampaign) {
        List<UserPerCampaignDTO> existingStudentList = userPerCampaignService
                .findByCampaignCode(updatedCampaign);

        List<UserPerCampaignDTO> studentsToRemove = existingStudentList.stream()
                .filter(student -> requestStudentList.stream()
                        .noneMatch(newStudent -> newStudent.getMemberCode().equals(student.getStudentCode())))
                .toList();

        List<MemberDTO> studentsToAdd = requestStudentList.stream()
                .filter(newStudent -> existingStudentList.stream()
                        .noneMatch(student -> student.getStudentCode().equals(newStudent.getMemberCode())))
                .toList();

        studentsToRemove.forEach(student -> userPerCampaignService
                .removeUserPerCampaign(student.getUserPerCampaignCode()));

        studentsToAdd.forEach(memberDTO -> {
            MemberDTO newStudent = memberService.findMemberByMemberCode(memberDTO.getMemberCode()
                    , memberDTO.getMemberType());
            if (newStudent == null) throw new CommonException(StatusEnum.STUDENT_NOT_FOUND);
            userPerCampaignService.registerUserPerCampaign(newStudent, requestCampaign);
        });
    }

    @Override
    public void removeCampaign(CampaignDTO request) {
        Campaign campaign = campaignRepository.findById(request.getCampaignCode())
                .orElseThrow(() -> new CommonException(StatusEnum.CAMPAIGN_NOT_FOUND));
        if (Objects.equals(request.getCampaignType(), CampaignTypeEnum.INSTANT.getType())) {
            throw new CommonException(StatusEnum.DELETE_NOT_ALLOWED);
        }

        campaignRepository.delete(campaign);
    }

    @Override
    public List<FindAllCampaignDTO> findAllCampaignList() {
        List<Campaign> campaigns = campaignRepository.findAll();
        List<FindAllCampaignDTO> findAllCampaignDTOList = new ArrayList<>();
        for (Campaign campaign : campaigns) {
            CampaignDTO campaignDTO = campaignMapper.toDTO(campaign);
            AdminDTO adminDTO = adminService.findByAdminCode(campaignDTO.getAdminCode());
            findAllCampaignDTOList.add(campaignMapper.toFindAllCampaignDTO(campaignDTO, adminDTO.getAdminName()));
        }

        return findAllCampaignDTOList;
    }

    @Override
    public CampaignDTO findCampaign(CampaignDTO request) {
        Campaign campaign = campaignRepository.findById(request.getCampaignCode())
                .orElseThrow(() -> new CommonException(StatusEnum.CAMPAIGN_NOT_FOUND));
        return campaignMapper.toDTO(campaign);
    }

    @Override
    public CampaignPageResponse<ResponseFindCampaignByConditionVO> findCampaignListByCondition
            (CampaignFilterDTO request, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        // 필터 조건과 페이징 처리된 데이터 조회
        Page<Campaign> campaignPage = campaignRepository.searchBy(request, pageable);

        List<ResponseFindCampaignByConditionVO> campaignDTOList = campaignPage.getContent().stream()
                .map(campaignMapper::fromCampaignToResponseFindCampaignByConditionVO)
                .collect(Collectors.toList());

        return new CampaignPageResponse<>(
                campaignDTOList,               // 데이터 리스트
                campaignPage.getTotalElements(), // 전체 데이터 수
                campaignPage.getTotalPages(),    // 전체 페이지 수
                campaignPage.getNumber() + 1,    // 현재 페이지 (0-based → 1-based)
                campaignPage.getSize()           // 페이지 크기
        );
    }
}
