package intbyte4.learnsmate.comment.mapper;

import intbyte4.learnsmate.comment.domain.dto.CommentDTO;
import intbyte4.learnsmate.comment.domain.entity.Comment;
import intbyte4.learnsmate.comment.domain.vo.request.ResponseFindCommentVO;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    // Comment -> CommentDTO 변환
    public CommentDTO fromCommentToCommentDTO(Comment comment) {
        return new CommentDTO(
                comment.getCommentCode(),
                comment.getCommentContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getMember().getMemberCode(),
                comment.getLecture().getLectureCode()
        );
    }

    // CommentDTO -> RequestFindCommentVO
    public ResponseFindCommentVO fromCommentDTOToResponseFindCommentVO(CommentDTO commentDTO) {
        return ResponseFindCommentVO.builder()
                .commentCode(commentDTO.getCommentCode())
                .commentContent(commentDTO.getCommentContent())
                .createdAt(commentDTO.getCreatedAt())
                .updatedAt(commentDTO.getUpdatedAt())
                .memberCode(commentDTO.getMemberCode())
                .lectureCode(commentDTO.getLectureCode())
                .build();
    }
}
