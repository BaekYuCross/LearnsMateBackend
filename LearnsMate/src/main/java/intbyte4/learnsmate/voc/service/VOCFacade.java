package intbyte4.learnsmate.voc.service;

import intbyte4.learnsmate.admin.domain.dto.AdminDTO;
import intbyte4.learnsmate.admin.service.AdminService;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.voc.domain.dto.VOCCategoryCountDTO;
import intbyte4.learnsmate.voc.domain.dto.VOCPageResponse;
import intbyte4.learnsmate.voc.domain.dto.VOCDTO;
import intbyte4.learnsmate.voc.domain.dto.VOCFilterRequestDTO;
import intbyte4.learnsmate.voc.domain.vo.response.ResponseFindVOCVO;
import intbyte4.learnsmate.voc.mapper.VOCMapper;
import intbyte4.learnsmate.voc_answer.domain.dto.VOCAnswerDTO;
import intbyte4.learnsmate.voc_answer.service.VOCAnswerService;
import intbyte4.learnsmate.voc_category.domain.dto.VOCCategoryDTO;
import intbyte4.learnsmate.voc_category.service.VOCCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VOCFacade {
    private final VOCService vocService;
    private final MemberService memberService;
    private final AdminService adminService;
    private final VOCAnswerService vocAnswerService;
    private final VOCCategoryService vocCategoryService;
    private final VOCMapper vocMapper;

    public ResponseFindVOCVO findVOC(String vocCode) {
        VOCDTO vocDTO = vocService.findByVOCCode(vocCode);
        MemberDTO memberDTO = memberService.findById(vocDTO.getMemberCode());
        VOCCategoryDTO categoryDTO = vocCategoryService.findByVocCategoryCode(vocDTO.getVocCategoryCode());
        VOCAnswerDTO vocAnswerDTO = vocAnswerService.findByVOCCode(vocDTO.getVocCode());
        if (vocAnswerDTO == null) return vocMapper.toUnansweredVOCResponseVO(vocDTO, memberDTO, categoryDTO);

        AdminDTO adminDTO = adminService.findByAdminCode(vocAnswerDTO.getAdminCode());

        return vocMapper.fromDTOToResponseVO(vocDTO, memberDTO, categoryDTO, vocAnswerDTO, adminDTO);
    }

    public VOCPageResponse<ResponseFindVOCVO> findVOCsByPage(int page, int size) {
        Page<VOCDTO> vocPage = vocService.findAllByVOCWithPaging(PageRequest.of(page, size));
        List<ResponseFindVOCVO> responseList = new ArrayList<>();

        for (VOCDTO vocDTO : vocPage.getContent()) {
            MemberDTO memberDTO = memberService.findById(vocDTO.getMemberCode());
            VOCCategoryDTO categoryDTO = vocCategoryService.findByVocCategoryCode(vocDTO.getVocCategoryCode());
            VOCAnswerDTO vocAnswerDTO = vocAnswerService.findByVOCCode(vocDTO.getVocCode());
            AdminDTO adminDTO = vocAnswerDTO != null ? adminService.findByAdminCode(vocAnswerDTO.getAdminCode()) : null;

            ResponseFindVOCVO responseVO = vocMapper.fromDTOToResponseVOAll(vocDTO, memberDTO, categoryDTO, adminDTO);
            responseList.add(responseVO);
        }

        return new VOCPageResponse<>(
                responseList,
                vocPage.getTotalElements(),
                vocPage.getTotalPages(),
                vocPage.getNumber(),
                vocPage.getSize()
        );
    }

    // 필터링x 정렬o
    public VOCPageResponse<ResponseFindVOCVO> findVOCsByPageWithSort(int page, int size, String sortField, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        Sort sort = Sort.by(direction, sortField);

        // PageRequest 객체에 정렬 정보 추가
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        // 정렬이 적용된 페이지 데이터 조회
        Page<VOCDTO> vocPage = vocService.findAllByVOCWithPagingWithSort(pageRequest);
        List<ResponseFindVOCVO> responseList = new ArrayList<>();

        // 연관 데이터 조회 및 응답 객체 생성
        for (VOCDTO vocDTO : vocPage.getContent()) {
            MemberDTO memberDTO = memberService.findById(vocDTO.getMemberCode());
            VOCCategoryDTO categoryDTO = vocCategoryService.findByVocCategoryCode(vocDTO.getVocCategoryCode());
            VOCAnswerDTO vocAnswerDTO = vocAnswerService.findByVOCCode(vocDTO.getVocCode());
            AdminDTO adminDTO = vocAnswerDTO != null ? adminService.findByAdminCode(vocAnswerDTO.getAdminCode()) : null;

            ResponseFindVOCVO responseVO = vocMapper.fromDTOToResponseVOAll(vocDTO, memberDTO, categoryDTO, adminDTO);
            responseList.add(responseVO);
        }

        // VOCPageResponse 객체 생성 및 반환
        return new VOCPageResponse<>(
                responseList,
                vocPage.getTotalElements(),
                vocPage.getTotalPages(),
                vocPage.getNumber(),
                vocPage.getSize()
        );
    }



    public VOCPageResponse<ResponseFindVOCVO> filterVOCsByPage(VOCFilterRequestDTO dto, int page, int size) {
        Page<VOCDTO> vocPage = vocService.filterVOCWithPaging(dto, PageRequest.of(page, size));
        List<ResponseFindVOCVO> responseList = new ArrayList<>();

        for (VOCDTO vocDTO : vocPage.getContent()) {
            MemberDTO memberDTO = memberService.findById(vocDTO.getMemberCode());
            VOCCategoryDTO categoryDTO = vocCategoryService.findByVocCategoryCode(vocDTO.getVocCategoryCode());
            VOCAnswerDTO vocAnswerDTO = vocAnswerService.findByVOCCode(vocDTO.getVocCode());
            AdminDTO adminDTO = vocAnswerDTO != null ? adminService.findByAdminCode(vocAnswerDTO.getAdminCode()) : null;

            ResponseFindVOCVO responseVO = vocMapper.fromDTOToResponseVOAll(vocDTO, memberDTO, categoryDTO, adminDTO);
            responseList.add(responseVO);
        }

        return new VOCPageResponse<>(
                responseList,
                vocPage.getTotalElements(),
                vocPage.getTotalPages(),
                vocPage.getNumber(),
                vocPage.getSize()
        );
    }

    public List<VOCCategoryCountDTO> getCategoryCounts() {
        return vocService.getCategoryCounts();
    }

    public List<VOCCategoryCountDTO> getFilteredCategoryCounts(LocalDateTime startDate, LocalDateTime endDate) {
        return vocService.getFilteredCategoryCounts(startDate, endDate);
    }

    public List<ResponseFindVOCVO> findAllVOCsByFilter(VOCFilterRequestDTO dto) {
        List<VOCDTO> vocList = vocService.findAllByFilter(dto); // 페이징 없이 전체 데이터 조회
        List<ResponseFindVOCVO> responseList = new ArrayList<>();

        for (VOCDTO vocDTO : vocList) {
            MemberDTO memberDTO = memberService.findById(vocDTO.getMemberCode());
            VOCCategoryDTO categoryDTO = vocCategoryService.findByVocCategoryCode(vocDTO.getVocCategoryCode());
            VOCAnswerDTO vocAnswerDTO = vocAnswerService.findByVOCCode(vocDTO.getVocCode());
            AdminDTO adminDTO = vocAnswerDTO != null ? adminService.findByAdminCode(vocAnswerDTO.getAdminCode()) : null;

            ResponseFindVOCVO responseVO = vocMapper.fromDTOToResponseVOAll(vocDTO, memberDTO, categoryDTO, adminDTO);
            responseList.add(responseVO);
        }

        return responseList;
    }

    public List<ResponseFindVOCVO> findAllVOCs() {
        List<VOCDTO> vocList = vocService.findAllVOCs();
        List<ResponseFindVOCVO> responseList = new ArrayList<>();

        for (VOCDTO vocDTO : vocList) {
            MemberDTO memberDTO = memberService.findById(vocDTO.getMemberCode());
            VOCCategoryDTO categoryDTO = vocCategoryService.findByVocCategoryCode(vocDTO.getVocCategoryCode());
            VOCAnswerDTO vocAnswerDTO = vocAnswerService.findByVOCCode(vocDTO.getVocCode());
            AdminDTO adminDTO = vocAnswerDTO != null ? adminService.findByAdminCode(vocAnswerDTO.getAdminCode()) : null;

            ResponseFindVOCVO responseVO = vocMapper.fromDTOToResponseVOAll(vocDTO, memberDTO, categoryDTO, adminDTO);
            responseList.add(responseVO);
        }

        return responseList;
    }

    public VOCDTO saveVOC(VOCDTO vocDto){
        MemberDTO memberDTO = memberService.findById(vocDto.getMemberCode());
        VOCCategoryDTO vocCategoryDto = vocCategoryService.findByVocCategoryCode(vocDto.getVocCategoryCode());

        VOCDTO responseDTO = vocService.saveVOC(vocDto, memberDTO, vocCategoryDto);

        return responseDTO;
    }
}