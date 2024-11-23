package intbyte4.learnsmate.lecture.pagination;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LectureCursorPaginationResponse<T> {
    private List<T> data;
    private LocalDateTime nextCursor;
    private int currentPage;
    private int pageSize;
    private boolean hasNext;
    private long totalElements;

    public static <T> LectureCursorPaginationResponse<T> ofCursor(List<T> data, LocalDateTime nextCursor, long totalElements) {
        return LectureCursorPaginationResponse.<T>builder()
                .data(data)
                .nextCursor(nextCursor)
                .hasNext(nextCursor != null)
                .totalElements(totalElements)
                .build();
    }

    public static <T> LectureCursorPaginationResponse<T> ofOffset(List<T> data, int currentPage, int pageSize, boolean hasNext, long totalElements) {
        return LectureCursorPaginationResponse.<T>builder()
                .data(data)
                .currentPage(currentPage)
                .pageSize(pageSize)
                .hasNext(hasNext)
                .totalElements(totalElements)
                .build();
    }
}
