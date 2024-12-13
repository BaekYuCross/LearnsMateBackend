package intbyte4.learnsmate.member.domain.vo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRatioFilterRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
