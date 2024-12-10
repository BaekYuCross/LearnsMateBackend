package intbyte4.learnsmate.campaign.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.domain.entity.Admin;
import intbyte4.learnsmate.admin.mapper.AdminMapper;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.admin.service.EmailService;
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
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.userpercampaign.domain.dto.UserPerCampaignDTO;
import intbyte4.learnsmate.userpercampaign.service.UserPerCampaignService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
    private final EmailService emailService;
    private final CampaignMapper campaignMapper;
    private final AdminMapper adminMapper;
    private final CampaignIssueCouponReader couponMemberReader;
    private final CoolSmsService coolSmsService;
    private final JobLauncher jobLauncher;
    private final Job campaignJob;

    @Override
    public CampaignDTO registerCampaign(CampaignDTO requestCampaign,
                                        List<MemberDTO> requestStudentList,
                                        List<CouponDTO> requestCouponList) {
        // 캠페인 기본 정보 설정
        Campaign campaign = createCampaign(requestCampaign);
        Campaign savedCampaign = campaignRepository.save(campaign);
        CampaignDTO savedCampaignDTO = campaignMapper.toDTO(savedCampaign);

        // 학생과 쿠폰 정보 등록
        registerStudentsAndCoupons(requestStudentList, requestCouponList, savedCampaignDTO);

        // 즉시 발송인 경우 바로 실행
        if (Objects.equals(requestCampaign.getCampaignType(), CampaignTypeEnum.INSTANT.getType())) {
            executeCampaign(savedCampaign);
        }

        return savedCampaignDTO;
    }

    private void registerStudentsAndCoupons(List<MemberDTO> requestStudentList,
                                            List<CouponDTO> requestCouponList,
                                            CampaignDTO savedCampaignDTO) {
        // 학생 검증 및 등록
        requestStudentList.forEach(memberDTO -> {
            MemberDTO foundStudent = memberService.findMemberByMemberCode(
                    memberDTO.getMemberCode(),
                    MemberType.STUDENT
            );
            if (foundStudent == null) {
                throw new CommonException(StatusEnum.STUDENT_NOT_FOUND);
            }
            userPerCampaignService.registerUserPerCampaign(foundStudent, savedCampaignDTO);
        });

        // 쿠폰 검증 및 등록
        requestCouponList.forEach(couponDTO -> {
            CouponDTO foundCoupon = couponService.findCouponDTOByCouponCode(couponDTO.getCouponCode());
            if (foundCoupon == null) {
                throw new CommonException(StatusEnum.COUPON_NOT_FOUND);
            } else if (foundCoupon.getTutorCode() != null) {
                throw new CommonException(StatusEnum.COUPON_CANNOT_BE_SENT_BY_TUTOR);
            }
            couponByCampaignService.registerCouponByCampaign(foundCoupon, savedCampaignDTO);
        });
    }

    private Campaign createCampaign(CampaignDTO requestCampaign) {
        AdminDTO adminDTO = adminService.findByAdminCode(requestCampaign.getAdminCode());
        Admin admin = adminMapper.toEntity(adminDTO);

        LocalDateTime sendTime = Objects.equals(requestCampaign.getCampaignType(),
                CampaignTypeEnum.INSTANT.getType()) ? LocalDateTime.now() : requestCampaign.getCampaignSendDate();

        return Campaign.builder()
                .campaignTitle(requestCampaign.getCampaignTitle())
                .campaignContents(requestCampaign.getCampaignContents())
                .campaignType(CampaignTypeEnum.valueOf(requestCampaign.getCampaignType()))
                .campaignMethod(requestCampaign.getCampaignMethod())
                .campaignSendDate(sendTime)
                .campaignSendFlag(false)
                .createdAt(requestCampaign.getCreatedAt())
                .updatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .admin(admin)
                .build();
    }

    @Override
    public void executeCampaign(Campaign campaign) {
        try {
            List<UserPerCampaignDTO> userList = userPerCampaignService.findUserByCampaignCode(campaign.getCampaignCode());
            List<MemberDTO> students = userList.stream()
                    .map(user -> memberService.findMemberByMemberCode(user.getStudentCode(), MemberType.STUDENT))
                    .toList();

            List<CouponByCampaignDTO> couponList = couponByCampaignService.findByCampaignCode(campaign);
            List<CouponDTO> coupons = couponList.stream()
                    .map(campaignCoupon -> couponService.findCouponDTOByCouponCode(campaignCoupon.getCouponCode()))
                    .toList();

            // 2. Reader에 데이터 설정
            log.info("Setting up reader with students: {}, coupons: {}", students.size(), coupons.size());
            couponMemberReader.setStudentCouponPairs(students, coupons);
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("campaignCode", campaign.getCampaignCode())
                    .addDate("startTime", new Date())
                    .toJobParameters();
            log.info("Executing campaign {}", campaign.getCampaignTitle());
            JobExecution jobExecution = jobLauncher.run(campaignJob, jobParameters);

            if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                sendNotifications(campaign);
                updateCampaignSendFlag(campaign.getCampaignCode());
            }
        } catch (Exception e) {
            log.error("Campaign execution failed for campaign: {}", campaign.getCampaignCode(), e);
            throw new CommonException(StatusEnum.CAMPAIGN_NOT_FOUND);
        }
    }

    private void sendNotifications(Campaign campaign) {
        List<UserPerCampaignDTO> userPerCampaignDTOList =
                userPerCampaignService.findUserByCampaignCode(campaign.getCampaignCode());

        for (UserPerCampaignDTO userPerCampaign : userPerCampaignDTOList) {
            MemberDTO member = memberService.findMemberByMemberCode(
                    userPerCampaign.getStudentCode(),
                    MemberType.STUDENT
            );

            if (member != null) {
                if (campaign.getCampaignMethod().equals("Email")) {
                    emailService.sendCampaignEmail(
                            member.getMemberEmail(),
                            campaign.getCampaignTitle(),
                            campaign.getCampaignContents()
                    );
                } else if (campaign.getCampaignMethod().equals("SMS")) {
                    coolSmsService.sendSms(
                            member.getMemberPhone(),
                            campaign.getCampaignContents()
                    );
                }
            }
        }
    }

    @Override
    public void registerScheduledCampaign(List<MemberDTO> studentList, List<CouponDTO> couponList, LocalDateTime scheduledTime) {
        System.out.println("캠페인 예약 작업이 등록되었습니다. 예약 시간: " + scheduledTime);
        try {
            couponMemberReader.setStudentCouponPairs(studentList, couponList);
        } catch (Exception e) {
            // 에러 처리
            System.err.println("캠페인 예약 작업 등록 실패: " + e.getMessage());
        }
    }



    @Override
    @Transactional
    public List<CampaignDTO> getReadyCampaigns(LocalDateTime currentTime) {
        // 예약 시간이 현재 시간보다 이전이면서 아직 발송되지 않은 캠페인 조회
        List<Campaign> readyCampaigns = campaignRepository
                .findByCampaignSendDateLessThanEqualAndCampaignSendFlagFalseAndCampaignType(currentTime, CampaignTypeEnum.RESERVATION);
        return readyCampaigns.stream()
                .map(campaignMapper::toDTO)
                .toList();
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
    public CampaignPageResponse<FindAllCampaignsDTO> findAllCampaignListBySort(
            int page, int size, String sortField, String sortDirection){

        if (sortField.equals("adminName")) {
            sortField = "admin.adminName";
        }

        Sort sort = Sort.by(
                sortDirection.equalsIgnoreCase("DESC") ?
                        Sort.Direction.DESC : Sort.Direction.ASC,
                sortField
        );
        PageRequest pageable = PageRequest.of(page, size, sort);
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
        AdminDTO adminDTO = adminService.findByAdminCode(campaignDTO.getAdminCode());
        Page<MemberDTO> membersPage = memberService.findMembersByCampaignCode(request, pageable);
        Page<CouponDTO> couponsPage = couponService.findCouponsByCampaignCode(request, pageable);

        return campaignMapper.toFindCampaignDetailDTO(campaignDTO,couponsPage,membersPage, adminDTO);
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
    public CampaignPageResponse<ResponseFindCampaignByFilterVO> findCampaignListByFilterAndSort(CampaignFilterDTO dto, int page, int size, String sortField, String sortDirection) {
        Sort sort = Sort.by(
                sortDirection.equalsIgnoreCase("DESC") ?
                        Sort.Direction.DESC : Sort.Direction.ASC,
                sortField
        );
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Campaign> campaignPage = campaignRepository.searchBy(dto, pageable);

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

    @Override
    public void updateCampaignSendFlag(Long campaignCode) {
        Campaign campaign = campaignRepository.findById(campaignCode)
                .orElseThrow(() -> new CommonException(StatusEnum.CAMPAIGN_NOT_FOUND));

        campaign.updateSendFlag();

        campaignRepository.save(campaign);
    }

}
