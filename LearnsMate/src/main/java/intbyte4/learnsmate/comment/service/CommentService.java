package intbyte4.learnsmate.comment.service;

import intbyte4.learnsmate.comment.domain.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> findAllComments();

    CommentDTO findComentByCommentCode(Long commentCode);
}
