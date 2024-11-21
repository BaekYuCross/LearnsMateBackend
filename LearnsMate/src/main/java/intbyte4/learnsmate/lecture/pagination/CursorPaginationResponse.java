package intbyte4.learnsmate.lecture.pagination;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CursorPaginationResponse<T> {
    private List<T> data;
    private LocalDateTime nextCursor;
}
