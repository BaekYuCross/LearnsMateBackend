package intbyte4.learnsmate.lecture.service;


import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.contractprocess.service.ContractProcessService;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.dto.TutorLectureVideoCountDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.repository.LectureRepository;
import intbyte4.learnsmate.lecture_category.domain.dto.LectureCategoryDTO;
import intbyte4.learnsmate.lecture_category.domain.entity.LectureCategory;
import intbyte4.learnsmate.lecture_category.mapper.LectureCategoryMapper;
import intbyte4.learnsmate.lecture_category.service.LectureCategoryService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import intbyte4.learnsmate.video_by_lecture.domain.dto.CountVideoByLectureDTO;
import intbyte4.learnsmate.video_by_lecture.service.VideoByLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final LectureMapper lectureMapper;
    private final VideoByLectureService videoByLectureService;
    private final LectureCategoryService lectureCategoryService;
    private final ContractProcessService contractProcessService;
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final LectureCategoryMapper lectureCategoryMapper;


    // 전체 강의 조회
    @Override
    public List<LectureDTO> getAllLecture() {
        List<Lecture> lectureList = lectureRepository.findAll();
        if (lectureList.isEmpty()) {
            throw new CommonException(StatusEnum.LECTURE_NOT_FOUND);
        }
        return lectureList.stream()
                .map(lectureMapper::toDTO)
                .collect(Collectors.toList());
    }


    // 강의 단건 조회
    @Override
    public LectureDTO getLectureById(Long lectureCode) {
        Lecture lecture = lectureRepository.findById(lectureCode)
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_NOT_FOUND));
        return lectureMapper.toDTO(lecture);
    }

    // 강사별 강의 모두 조회
    @Override
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


    // 카테고리별 강의 조회
//    public List<Lecture> filterLectures(LectureFilterDTO filter) {
//        Specification<Lecture> spec = LectureSpecifications.filterByCriteria(filter);
//        return lectureRepository.findAll(spec);
//    }


    // 강의 등록
    @Override
    @Transactional
    public LectureDTO registerLecture(LectureDTO lectureDTO) {

//        // Lecture 엔티티 빌드
//        Lecture lecture = Lecture.builder()
//                .lectureTitle(lectureDTO.getLectureTitle())
//                .lectureCategoryEnum(lectureDTO.getLectureCategoryEnum())
//                .lectureConfirmStatus(false) // 계약과정에서 승인 과정이 7이면 true로 변경
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .lectureImage(lectureDTO.getLectureImage())
//                .lecturePrice(lectureDTO.getLecturePrice())
//                .tutorCode(memberService.findByMemberCode(lectureDTO.getTutorCode()))
//                .lectureStatus(true)
//                .lectureClickCount(0)
//                .lectureLevel(lectureDTO.getLectureLevel())
//                .build();
//
//
//        lectureRepository.save(lecture);
//
//        contractProcessService.createContractProcess(lecture.getLectureCode(), contractProcessDTO);
//
//        return lectureMapper.toDTO(lecture);
        return null;
    }


    // 강의 수정
    @Override
    @Transactional
    public LectureDTO updateLecture(Long lectureCode, LectureDTO lectureDTO) {

        Lecture lecture = lectureRepository.findById(lectureCode)
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_NOT_FOUND));

        LectureCategoryDTO lectureCategoryDTO =
                lectureCategoryService.findByLectureCategoryCode(lectureDTO.getLectureCategoryCode());
        LectureCategory lectureCategory = lectureCategoryMapper.toEntity(lectureCategoryDTO);

        lecture.toUpdate(lectureDTO, lectureCategory);
        lectureRepository.save(lecture);
        return lectureMapper.toDTO(lecture);
    }


    // 강의 삭제
    @Override
    @Transactional
    public LectureDTO removeLecture(Long lectureCode) {
        Lecture lecture = lectureRepository.findById(lectureCode)
                .orElseThrow(() -> new CommonException(StatusEnum.LECTURE_NOT_FOUND));
        lecture.toDelete();
        lectureRepository.save(lecture);
        return lectureMapper.toDTO(lecture);
    }

    // 강사별 강의와 강의별 동영상 개수 조회 서비스 메서드
    @Override
    public List<TutorLectureVideoCountDTO> getVideoCountByLecture(Long tutorCode) {
        List<LectureDTO> lectures = getLecturesByTutorCode(tutorCode);
        // 강의별 동영상 개수 조회 + 강의 타이틀 담기
        return lectures.stream()
                .map(lecture -> {
                    // 각 강의에 대한 동영상 개수 조회
                    CountVideoByLectureDTO videoCountDTO =
                            videoByLectureService.getVideoByLecture(lecture.getLectureCode());

                    // 강의명과 동영상 개수를 DTO로 반환
                    return TutorLectureVideoCountDTO.builder()
                            .lectureCode(lecture.getLectureCode())
                            .lectureTitle(lecture.getLectureTitle())
                            .videoCount(videoCountDTO.getVideoCount())
                            .build();
                })
                .collect(Collectors.toList());
    }

}
