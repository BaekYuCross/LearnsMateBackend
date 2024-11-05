package intbyte4.learnsmate.comment.domain.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long commentCode;
    private String commentContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long memberCode;
    private Long lectureCode;
}
