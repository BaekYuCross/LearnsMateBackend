package intbyte4.learnsmate.comment.controller;

import intbyte4.learnsmate.comment.domain.dto.CommentDTO;
import intbyte4.learnsmate.comment.domain.vo.request.ResponseFindCommentVO;
import intbyte4.learnsmate.comment.mapper.CommentMapper;
import intbyte4.learnsmate.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Operation(summary = "모든 댓글 조회")
    @GetMapping
    public ResponseEntity<List<ResponseFindCommentVO>> findAllComments() {
        List<CommentDTO> commentDTOList = commentService.findAllComments();

        return ResponseEntity.status(HttpStatus.OK).body(commentDTOList.stream()
                .map(commentMapper::fromCommentDTOToResponseFindCommentVO)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "댓글 단건 조회")
    @GetMapping("/{commentcode}")
    public ResponseEntity<ResponseFindCommentVO> findCommentByCommentCode(@PathVariable("commentcode") Long commentCode) {
        CommentDTO commentDTO = commentService.findComentByCommentCode(commentCode);

        return ResponseEntity.status(HttpStatus.OK)
                .body(commentMapper.fromCommentDTOToResponseFindCommentVO(commentDTO));
    }


    @Operation(summary = "강의별 댓글 조회")
    @GetMapping("/lecture/{lectureCode}")
    public ResponseEntity<List<ResponseFindCommentVO>> findCommentByLectureCode(@PathVariable("lectureCode") Long lectureCode ) {
        List<CommentDTO> commentDTOList = commentService.findCommentByLectureCode(lectureCode);

        return ResponseEntity.status(HttpStatus.OK).body(commentDTOList.stream()
                .map(commentMapper::fromCommentDTOToResponseFindCommentVO)
                .collect(Collectors.toList()));
    }
}
