package intbyte4.learnsmate.member.domain.vo.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestFilterTutorVO {

    private Long memberCode;
    private String memberName;           // 이름
    private String memberPhone;          // 연락처
    private String memberEmail;          // 이메일
    private String memberAddress;        // 주소
    private Integer memberAge;           // 나이
    private LocalDateTime birthStartDate; // 생년월일 시작 범위
    private LocalDateTime birthEndDate;   // 생년월일 종료 범위
    private String memberStatus;         // 계정 상태
}
