package intbyte4.learnsmate.member.domain.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class MemberPageResponse<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int size;
    private Long nextCursor;  // LocalDateTime에서 Long으로 변경

    // 기존 생성자도 유지 (nextCursor 없는 버전)
    public MemberPageResponse(List<T> content, long totalElements, int totalPages, int currentPage, int size) {
        this(content, totalElements, totalPages, currentPage, size, null);
    }
}
