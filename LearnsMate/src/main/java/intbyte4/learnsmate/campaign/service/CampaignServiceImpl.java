package intbyte4.learnsmate.campaign.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.campaign.batch.CampaignIssueCouponReader;
import intbyte4.learnsmate.campaign.domain.dto.*;
import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.campaign.domain.entity.CampaignTypeEnum;
import intbyte4.learnsmate.campaign.domain.vo.response.ResponseFindCampaignByFilterVO;
import intbyte4.learnsmate.campaign.mapper.CampaignMapper;
import intbyte4.learnsmate.campaign.repository.CampaignRepository;

import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.coupon.domain.dto.CouponDTO;
import intbyte4.learnsmate.coupon.service.CouponService;
import intbyte4.learnsmate.couponbycampaign.domain.dto.CouponByCampaignDTO;
import intbyte4.learnsmate.couponbycampaign.service.CouponByCampaignService;
import intbyte4.learnsmate.issue_coupon.service.IssueCouponService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.userpercampaign.domain.dto.UserPerCampaignDTO;
import intbyte4.learnsmate.userpercampaign.service.UserPerCampaignService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("campaignService")
@RequiredArgsConstructor
@Slf4j
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository campaignRepository;
    private final AdminService adminService;
    private final UserPerCampaignService userPerCampaignService;
    private final CouponByCampaignService couponByCampaignService;
    private final MemberService memberService;
    private final CouponService couponService;
    private final IssueCouponService issueCouponService;
    private final CampaignMapper campaignMapper;
    private final AdminMapper adminMapper;
    private final JobLauncher jobLauncher;
    private final Job campaignJob;
    private final CampaignIssueCouponReader couponMemberReader;

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
                    , MemberType.STUDENT);
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
        List<Long> studentCodes = requestStudentList.stream()
                .map(MemberDTO::getMemberCode)
                .toList();

        List<Long> couponCodes = requestCouponList.stream()
                .map(CouponDTO::getCouponCode)
                .toList();

        if (Objects.equals(requestCampaign.getCampaignType(), CampaignTypeEnum.INSTANT.getType())) {
            // 즉시 발송
            issueCouponService.issueCouponsToStudents(studentCodes, couponCodes);
        } else {
            // 예약 발송은 스케줄러에 등록 아직
            registerScheduledCampaign(requestStudentList, requestCouponList, savedCampaignDTO.getCampaignSendDate());
        }

        return campaignMapper.toDTO(campaign);
    }

    @Override
    public void registerScheduledCampaign(List<MemberDTO> studentList, List<CouponDTO> couponList, LocalDateTime scheduledTime) {
        System.out.println("캠페인 예약 작업이 등록되었습니다. 예약 시간: " + scheduledTime);
        try {

            couponMemberReader.setStudentCouponPairs(studentList, couponList);


            // Job 파라미터 설정: 예약 시간과 관련된 정보를 파라미터에 추가
            JobParameters jobParameters = new JobParametersBuilder()
                    .addDate("scheduledTime", Timestamp.valueOf(scheduledTime))
                    .toJobParameters();

            // Job 실행
            jobLauncher.run(campaignJob, jobParameters);
        } catch (Exception e) {
            // 에러 처리
            System.err.println("캠페인 예약 작업 등록 실패: " + e.getMessage());
        }
    }



    @Override
    @Transactional
    public List<CampaignDTO> getReadyCampaigns(LocalDateTime currentTime) {
        // 예약 시간이 현재 시간보다 이전이면서 아직 발송되지 않은 캠페인 조회
        List<Campaign> readyCampaigns = campaignRepository.findByCampaignSendDateLessThanEqual(currentTime);
        return readyCampaigns.stream()
                .map(campaignMapper::toDTO)
                .toList();
    }

    @Override
    public CampaignDTO editCampaign(CampaignDTO requestCampaign
            , List<MemberDTO> requestStudentList
            , List<CouponDTO> requestCouponList) {
        log.info("서비스에서 조회하는 캠페인 코드: {}", requestCampaign.getCampaignCode());
        if (requestCampaign.getCampaignCode() == null) {
            throw new CommonException(StatusEnum.CAMPAIGN_NOT_FOUND);
        }
        if (Objects.equals(requestCampaign.getCampaignType(), CampaignTypeEnum.INSTANT.getType())) {
            throw new CommonException(StatusEnum.UPDATE_NOT_ALLOWED);
        }

        AdminDTO adminDTO = adminService.findByAdminCode(requestCampaign.getAdminCode());
        Admin admin = adminMapper.toEntity(adminDTO);
        log.info("서비스에서 조회하는 adminDTO: {}", adminDTO);
        Campaign updatedCampaign = campaignMapper.toEntity(requestCampaign, admin);
        log.info("서비스에서 조회하는 toEntity(requestCampaign,admin): {}", updatedCampaign);
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
        log.info("editStuden메서드의 requestCampaign: {}", requestCampaign);
        log.info("editStuden메서드의 requestStudentList: {}", requestStudentList);
        log.info("editStuden메서드의 updatedCampaign: {}", updatedCampaign);
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
    @Transactional
    @Override
    public void removeCampaign(CampaignDTO request) {
        userPerCampaignService.removeByCampaignCode(request.getCampaignCode());

        couponByCampaignService.removeByCampaignCode(request.getCampaignCode());

        Campaign campaign = campaignRepository.findById(request.getCampaignCode())
                .orElseThrow(() -> new CommonException(StatusEnum.CAMPAIGN_NOT_FOUND));
        if (Objects.equals(request.getCampaignType(), CampaignTypeEnum.INSTANT.getType())) {
            throw new CommonException(StatusEnum.DELETE_NOT_ALLOWED);
        }

        campaignRepository.delete(campaign);
    }

    @Override
    public CampaignPageResponse<FindAllCampaignsDTO> findAllCampaignList(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Campaign> campaignPage = campaignRepository.findAll(pageable);
        List<FindAllCampaignsDTO> findAllCampaignsDTOList = new ArrayList<>();
        for (Campaign campaign : campaignPage) {
            CampaignDTO campaignDTO = campaignMapper.toDTO(campaign);
            AdminDTO adminDTO = adminService.findByAdminCode(campaignDTO.getAdminCode());
            findAllCampaignsDTOList.add(campaignMapper.toFindAllCampaignDTO(campaignDTO, adminDTO.getAdminName()));
        }

        return new CampaignPageResponse<>(
                findAllCampaignsDTOList,
                campaignPage.getTotalElements(),
                campaignPage.getTotalPages(),
                campaignPage.getNumber(),
                campaignPage.getSize()
        );
    }

    @Override
    public FindCampaignDetailDTO findCampaign(FindCampaignDetailDTO request, int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        Campaign campaign = campaignRepository.findById(request.getCampaignCode())
                .orElseThrow(() -> new CommonException(StatusEnum.CAMPAIGN_NOT_FOUND));
        CampaignDTO campaignDTO = campaignMapper.toDTO(campaign);

        Page<MemberDTO> membersPage = memberService.findMembersByCampaignCode(request, pageable);
        Page<CouponDTO> couponsPage = couponService.findCouponsByCampaignCode(request, pageable);

        return campaignMapper.toFindCampaignDetailDTO(campaignDTO,couponsPage,membersPage);
    }

    @Override
    public CampaignPageResponse<ResponseFindCampaignByFilterVO> findCampaignListByFilter
            (CampaignFilterDTO request, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        // 필터 조건과 페이징 처리된 데이터 조회
        Page<Campaign> campaignPage = campaignRepository.searchBy(request, pageable);

        List<ResponseFindCampaignByFilterVO> campaignDTOList = campaignPage.getContent().stream()
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

    @Override
    public List<FindAllCampaignsDTO> findCampaignListByConditionWithExcel(CampaignFilterDTO filterDTO) {
        List<Campaign> campaignList = campaignRepository.searchByWithoutPaging(filterDTO);

        return campaignList.stream()
                .map(campaignMapper::fromCampaignToFindAllCampaignDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FindAllCampaignsDTO> findAllCampaignListWithExcel() {
        List<Campaign> campaigns = campaignRepository.findAll();
        List<FindAllCampaignsDTO> findAllCampaignsDTOList = new ArrayList<>();
        for (Campaign campaign : campaigns) {
            CampaignDTO campaignDTO = campaignMapper.toDTO(campaign);
            AdminDTO adminDTO = adminService.findByAdminCode(campaignDTO.getAdminCode());
            findAllCampaignsDTOList.add(campaignMapper.toFindAllCampaignDTO(campaignDTO, adminDTO.getAdminName()));
        }

        return findAllCampaignsDTOList;
    }
}
