package intbyte4.learnsmate.blacklist.domain.vo.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseFindReservedStudentBlacklistVO {
    // 학생코드, 학생명, 누적 신고 횟수 이렇게가 필요함. -> dto 하나 만들자.
    private Long memberCode;
    private String memberName;
    private Integer reportCount;
}
