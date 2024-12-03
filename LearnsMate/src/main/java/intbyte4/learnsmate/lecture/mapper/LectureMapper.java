package intbyte4.learnsmate.lecture.mapper;

import intbyte4.learnsmate.coupon.domain.vo.request.LectureRequestVO;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.dto.LectureFilterDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.domain.entity.LectureLevelEnum;
import intbyte4.learnsmate.lecture.domain.vo.request.RequestEditLectureInfoVO;
import intbyte4.learnsmate.lecture.domain.vo.request.RequestLectureFilterVO;
import intbyte4.learnsmate.lecture.domain.vo.request.RequestRegisterLectureVO;
import intbyte4.learnsmate.lecture.domain.vo.response.*;
import intbyte4.learnsmate.lecture_category.domain.dto.LectureCategoryDTO;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.payment.domain.vo.RequestRegisterLecturePaymentVO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LectureMapper {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    public LectureMapper(MemberService memberService, MemberMapper memberMapper) {
        this.memberService = memberService;
        this.memberMapper = memberMapper;
    }

    public LectureDTO toDTO(Lecture entity) {
        return LectureDTO.builder()
                .lectureCode(entity.getLectureCode())
                .lectureTitle(entity.getLectureTitle())
                .lectureConfirmStatus(entity.getLectureConfirmStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .lectureImage(entity.getLectureImage())
                .lecturePrice(entity.getLecturePrice())
                .lectureStatus(entity.getLectureStatus())
                .lectureClickCount(entity.getLectureClickCount())
                .lectureLevel(String.valueOf(entity.getLectureLevel()))
                .tutorCode(entity.getTutor().getMemberCode())
                .build();
    }

    public Lecture toEntity(LectureDTO dto, Member tutor) {
        return Lecture.builder()
                .lectureCode(dto.getLectureCode())
                .lectureTitle(dto.getLectureTitle())
                .lectureConfirmStatus(dto.getLectureConfirmStatus())
                .createdAt(dto.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .lectureImage(dto.getLectureImage())
                .lecturePrice(dto.getLecturePrice())
                .lectureStatus(dto.getLectureStatus())
                .lectureClickCount(dto.getLectureClickCount())
                .lectureLevel(LectureLevelEnum.valueOf(dto.getLectureLevel()))
                .tutor(tutor)
                .build();
    }

    public LectureDTO fromRequestVOtoDto(RequestEditLectureInfoVO vo) {
        return LectureDTO.builder()
                .lectureTitle(vo.getLectureTitle())
                .updatedAt(LocalDateTime.now())
                .lectureImage(vo.getLectureImage())
                .lecturePrice(vo.getLecturePrice())
                .lectureLevel(String.valueOf(vo.getLectureLevel()))
                .build();
    }

    public LectureDTO fromRegisterRequestVOtoDto(RequestRegisterLectureVO vo) {
        return LectureDTO.builder()
                .lectureTitle(vo.getLectureTitle())
                .lectureConfirmStatus(vo.getLectureConfirmStatus())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lectureImage(vo.getLectureImage())
                .lecturePrice(vo.getLecturePrice())
                .lectureStatus(vo.getLectureStatus())
                .lectureClickCount(0)
                .lectureLevel(String.valueOf(vo.getLectureLevel()))
                .tutorCode(vo.getTutorCode())
                .build();
    }

    public ResponseEditLectureInfoVO fromDtoToEditResponseVO(LectureDTO updatedLecture) {
        return ResponseEditLectureInfoVO.builder()
                .lectureTitle(updatedLecture.getLectureTitle())
                .lectureConfirmStatus(updatedLecture.getLectureConfirmStatus())
                .updatedAt(updatedLecture.getUpdatedAt())
                .lectureImage(updatedLecture.getLectureImage())
                .lecturePrice(updatedLecture.getLecturePrice())
                .lectureStatus(updatedLecture.getLectureStatus())
                .lectureClickCount(updatedLecture.getLectureClickCount())
                .lectureLevel(LectureLevelEnum.valueOf(updatedLecture.getLectureLevel()))
                .build();
    }

    public ResponseRegisterLectureVO fromDtoToRegisterResponseVO(LectureDTO Lecture) {
        return ResponseRegisterLectureVO.builder()
                .lectureTitle(Lecture.getLectureTitle())
                .lectureConfirmStatus(Lecture.getLectureConfirmStatus())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lectureImage(Lecture.getLectureImage())
                .lecturePrice(Lecture.getLecturePrice())
                .lectureStatus(Lecture.getLectureStatus())
                .lectureClickCount(0)
                .lectureLevel(LectureLevelEnum.valueOf(Lecture.getLectureLevel()))
                .tutorCode(Lecture.getTutorCode())
                .build();
    }

    public ResponseRemoveLectureVO fromDtoToRemoveResponseVO(LectureDTO removedLecture) {
        return ResponseRemoveLectureVO.builder()
                .lectureCode(removedLecture.getLectureCode())
                .lectureTitle(removedLecture.getLectureTitle())
                .lectureConfirmStatus(removedLecture.getLectureConfirmStatus())
                .createdAt(removedLecture.getCreatedAt())
                .updatedAt(removedLecture.getUpdatedAt())
                .lectureImage(removedLecture.getLectureImage())
                .lecturePrice(removedLecture.getLecturePrice())
                .tutorCode(removedLecture.getTutorCode())
                .lectureStatus(removedLecture.getLectureStatus())
                .lectureClickCount(removedLecture.getLectureClickCount())
                .lectureLevel(LectureLevelEnum.valueOf(removedLecture.getLectureLevel()))
                .build();
    }

    public LectureDTO fromRequestRegisterLecturePaymentVOToDTO(RequestRegisterLecturePaymentVO vo) {
        return LectureDTO.builder()
                .lectureTitle(vo.getLectureTitle())
                .lectureConfirmStatus(vo.getLectureConfirmStatus())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lectureImage(vo.getLectureImage())
                .lecturePrice(vo.getLecturePrice())
                .lectureStatus(vo.getLectureStatus())
                .lectureClickCount(0)
                .lectureLevel(String.valueOf(vo.getLectureLevel()))
                .build();
    }

    public LectureFilterDTO toFilterDTO(RequestLectureFilterVO request) {
        return LectureFilterDTO.builder()
                .lectureCode(request.getLectureCode())
                .lectureTitle(request.getLectureTitle())
                .tutorCode(request.getTutorCode())
                .tutorName(request.getTutorName())
                .lectureCategoryName(request.getLectureCategoryName())
                .lectureLevel(request.getLectureLevel())
                .lectureConfirmStatus(request.getLectureConfirmStatus())
                .lectureStatus(request.getLectureStatus())
                .minPrice(request.getMinPrice())
                .maxPrice(request.getMaxPrice())
                .startCreatedAt(request.getStartCreatedAt())
                .endCreatedAt(request.getEndCreatedAt())
                .build();
    }

    public ResponseFindLectureVO fromDTOToResponseVOAll(LectureDTO lectureDTO, MemberDTO tutorDTO, LectureCategoryDTO lectureCategoryDTO) {
        return ResponseFindLectureVO.builder()
                .lectureCode(lectureDTO.getLectureCode())
                .lectureTitle(lectureDTO.getLectureTitle())
                .tutorCode(lectureDTO.getTutorCode())
                .tutorName(tutorDTO.getMemberName())
                .lectureCategoryName(lectureCategoryDTO.getLectureCategoryName())
                .lectureLevel(lectureDTO.getLectureLevel())
                .lectureConfirmStatus(lectureDTO.getLectureConfirmStatus())
                .lectureStatus(lectureDTO.getLectureStatus())
                .createdAt(lectureDTO.getCreatedAt())
                .lecturePrice(lectureDTO.getLecturePrice())
                .build();
    }

    public LectureDTO convertToLectureDTO(ResponseFindLectureVO vo) {
        return LectureDTO.builder()
                .lectureCode(vo.getLectureCode())
                .lectureTitle(vo.getLectureTitle())
                .lectureConfirmStatus(vo.getLectureConfirmStatus())
                .createdAt(vo.getCreatedAt())
                .updatedAt(null)
                .lectureImage(null)
                .lecturePrice(vo.getLecturePrice())
                .tutorCode(vo.getTutorCode())
                .lectureStatus(vo.getLectureStatus())
                .lectureClickCount(0)
                .lectureLevel(vo.getLectureLevel())
                .build();
    }

    public Lecture fromDTOToEntity(LectureDTO lectureDTO) {
        return Lecture.builder()
                .lectureCode(lectureDTO.getLectureCode())
                .lectureTitle(lectureDTO.getLectureTitle())
                .lectureConfirmStatus(lectureDTO.getLectureConfirmStatus())
                .createdAt(lectureDTO.getCreatedAt())
                .updatedAt(null)
                .lectureImage(null)
                .lecturePrice(lectureDTO.getLecturePrice())
                .tutor(memberMapper.fromMemberDTOtoMember(memberService.findMemberByMemberCode(lectureDTO.getTutorCode(), MemberType.TUTOR)))
                .lectureStatus(lectureDTO.getLectureStatus())
                .lectureClickCount(lectureDTO.getLectureClickCount())
                .lectureLevel(LectureLevelEnum.valueOf(lectureDTO.getLectureLevel()))
                .build();
    }

    public LectureDTO requestVOToDTO(LectureRequestVO request) {
        return LectureDTO.builder()
                .lectureCode(request.getLectureCode())
                .lectureTitle(request.getLectureTitle())
                .lectureConfirmStatus(request.getLectureConfirmStatus())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .lectureImage(request.getLectureImage())
                .lecturePrice(request.getLecturePrice())
                .tutorCode(request.getTutorCode())
                .lectureStatus(request.getLectureStatus())
                .lectureClickCount(request.getLectureClickCount())
                .lectureLevel(request.getLectureLevel())
                .build();
    }

    public List<ResponseClientFindLectureVO> fromDTOtoResponseClinetFindLectureVO(List<LectureDTO> dtoList) {
        return dtoList.stream()
                .map(dto -> ResponseClientFindLectureVO.builder()
                        .lectureCode(dto.getLectureCode())
                        .lectureTitle(dto.getLectureTitle())
                        .lectureConfirmStatus(dto.getLectureConfirmStatus())
                        .createdAt(dto.getCreatedAt())
                        .updatedAt(dto.getUpdatedAt())
                        .lectureImage(dto.getLectureImage())
                        .lecturePrice(dto.getLecturePrice())
                        .tutorCode(dto.getTutorCode())
                        .lectureStatus(dto.getLectureStatus())
                        .lectureClickCount(dto.getLectureClickCount())
                        .lectureLevel(dto.getLectureLevel())
                        .build())
                .collect(Collectors.toList());
    }
}
