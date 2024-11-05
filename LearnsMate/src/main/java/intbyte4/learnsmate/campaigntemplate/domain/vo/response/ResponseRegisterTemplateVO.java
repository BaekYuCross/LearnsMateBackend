package intbyte4.learnsmate.campaigntemplate.domain.vo.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Builder
public class ResponseRegisterTemplateVO {

    private final Long campaignTemplateCode;
    private final String campaignTemplateTitle;
    private final String campaignTemplateContents;
    private final Boolean campaignTemplateFlag;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Long adminCode;
}
