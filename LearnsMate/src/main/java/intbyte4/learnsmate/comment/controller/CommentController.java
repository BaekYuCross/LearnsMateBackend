package intbyte4.learnsmate.comment.controller;

import intbyte4.learnsmate.comment.domain.dto.CommentDTO;
import intbyte4.learnsmate.comment.mapper.CommentMapper;
import intbyte4.learnsmate.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    // 1. comment 모두 조회 -> 사실 이게 우리쪽에서 comment를 봐야하는가? 봐야하지 않을까? 블랙리스트에서 어떤 코멘트가 신고먹었는지는?
    @GetMapping
    public ResponseEntity<?> findAllComments() {
        List<CommentDTO> commentDTOList = commentService.findAllComments();

        return ResponseEntity.status(HttpStatus.OK).body(commentDTOList.stream()
                .map(commentMapper::fromCommentDTOToResponseFindCommentVO)
                .collect(Collectors.toList()));
    }

    // 1-2. commentCode로 단건 조회
    @GetMapping("/{commentcode}")
    public ResponseEntity<?> findCommentByCommentCode(@PathVariable("commentcode") Long commentCode) {
        CommentDTO commentDTO = commentService.findComentByCommentCode(commentCode);

        return ResponseEntity.status(HttpStatus.OK)
                .body(commentMapper.fromCommentDTOToResponseFindCommentVO(commentDTO));
    }
}
