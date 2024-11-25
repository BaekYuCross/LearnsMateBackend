package intbyte4.learnsmate.member.domain.vo.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import intbyte4.learnsmate.member.domain.MemberType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestFilterMemberVO {

    private Long memberCode;
    private String memberName;
    private MemberType memberType;
    private String memberEmail;
    private String memberPhone;
    private String memberAddress;

    // 나이 범위
    private Integer memberStartAge;
    private Integer memberEndAge;

    // 생년월일 범위
    private LocalDateTime birthStartDate;
    private LocalDateTime birthEndDate;

    private Boolean memberFlag;
    private Boolean memberDormantFlag;

    // 생성일 범위
    private LocalDateTime createdStartDate;
    private LocalDateTime createdEndDate;
}
