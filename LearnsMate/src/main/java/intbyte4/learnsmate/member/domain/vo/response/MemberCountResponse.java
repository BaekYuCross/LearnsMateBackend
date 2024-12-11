package intbyte4.learnsmate.member.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberCountResponse {
    private int yesterdayCount; // 어제 가입한 회원 수
    private int totalCount;
}
