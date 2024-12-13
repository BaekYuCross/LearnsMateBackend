package intbyte4.learnsmate.voc.domain.vo.request;

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
public class RequestFilterVOCVO {
    private String vocCode;
    private String vocContent;
    private Integer vocCategoryCode;
    private String memberType;
    private Boolean vocAnswerStatus;
    private String vocAnswerSatisfaction;
    private LocalDateTime startCreateDate;
    private LocalDateTime startEndDate;
}
