package intbyte4.learnsmate.campaigntemplate.domain.vo.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class RequestRegisterTemplateVO {

    private final Long campaignTemplateCode;
    private final String campaignTemplateTitle;
    private final String campaignTemplateContents;
    private final Boolean campaignTemplateFlag;
    private final Long adminCode;
}
