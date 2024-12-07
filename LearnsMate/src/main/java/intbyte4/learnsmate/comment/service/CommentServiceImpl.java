package intbyte4.learnsmate.comment.service;

import intbyte4.learnsmate.comment.domain.dto.CommentDTO;
import intbyte4.learnsmate.comment.domain.entity.Comment;
import intbyte4.learnsmate.comment.mapper.CommentMapper;
import intbyte4.learnsmate.comment.repository.CommentRepository;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import intbyte4.learnsmate.lecture.domain.dto.LectureDTO;
import intbyte4.learnsmate.lecture.domain.entity.Lecture;
import intbyte4.learnsmate.lecture.mapper.LectureMapper;
import intbyte4.learnsmate.lecture.service.LectureService;
import intbyte4.learnsmate.member.domain.MemberType;
import intbyte4.learnsmate.member.domain.dto.MemberDTO;
import intbyte4.learnsmate.member.domain.entity.Member;
import intbyte4.learnsmate.member.mapper.MemberMapper;
import intbyte4.learnsmate.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final LectureService lectureService;
    private final LectureMapper lectureMapper;
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @Override
    public List<CommentDTO> findAllComments() {
        List<Comment> commentList = commentRepository.findAll();

        return commentList.stream()
                .map(commentMapper::fromCommentToCommentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO findCommentByCommentCode(Long commentCode) {
        Comment comment = commentRepository.findById(commentCode)
                .orElseThrow(() -> new CommonException(StatusEnum.COMMENT_NOT_FOUND));

        return commentMapper.fromCommentToCommentDTO(comment);
    }

    @Override
    public List<CommentDTO> findCommentByLectureCode(String lectureCode) {
        LectureDTO lectureDTO = lectureService.getLectureById(lectureCode);

        MemberDTO tutorDTO = memberService.findMemberByMemberCode(lectureDTO.getTutorCode(), MemberType.TUTOR);
        Member tutor = memberMapper.fromMemberDTOtoMember(tutorDTO);
        Lecture lecture = lectureMapper.toEntity(lectureDTO, tutor);

        List<Comment> commentList = commentRepository.findByLecture(lecture);

        return  commentList.stream()
                .map(commentMapper::fromCommentToCommentDTO)
                .collect(Collectors.toList());
    }
}
