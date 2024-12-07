package intbyte4.learnsmate.voc.domain.vo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VOCCategoryRatioFilterRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}