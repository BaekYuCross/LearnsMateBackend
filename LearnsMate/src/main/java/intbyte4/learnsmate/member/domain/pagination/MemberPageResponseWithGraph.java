package intbyte4.learnsmate.member.domain.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberPageResponseWithGraph<T, G> {
    private MemberPageResponse<T> pageResponse;
    private G graphData;
}
