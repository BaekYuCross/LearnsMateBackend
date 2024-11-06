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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestSaveMemberVO {
    private Long memberCode;
    private MemberType memberType;
    private String memberEmail;
    private String memberPassword;
    private String memberName;
    private Integer memberAge;
    private String memberPhone;
    private String memberAddress;
    private LocalDateTime memberBirth;
    private Boolean memberFlag;
    private Boolean memberDormantStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
