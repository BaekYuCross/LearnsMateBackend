package intbyte4.learnsmate.comment.service;


import intbyte4.learnsmate.comment.domain.dto.CommentDTO;
import intbyte4.learnsmate.comment.domain.entity.Comment;
import intbyte4.learnsmate.comment.mapper.CommentMapper;
import intbyte4.learnsmate.comment.repository.CommentRepository;
import intbyte4.learnsmate.common.exception.CommonException;
import intbyte4.learnsmate.common.exception.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    // 모든 댓글 조회
    public List<CommentDTO> findAllComments() {
        // 1. repo에 있는 모든 데이터 조회하면 됨.
        List<Comment> commentList = commentRepository.findAll();

        return commentList.stream()
                .map(commentMapper::fromCommentToCommentDTO)
                .collect(Collectors.toList());
    }

    @Override
    // 댓글 1개 조회
    public CommentDTO findComentByCommentCode(Long commentCode) {
        Comment comment = commentRepository.findById(commentCode)
                .orElseThrow(() -> new CommonException(StatusEnum.COMMENT_NOT_FOUND));

        return commentMapper.fromCommentToCommentDTO(comment);
    }
}
