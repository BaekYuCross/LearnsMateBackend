package intbyte4.learnsmate.campaigntemplate.domain.vo.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ResponseRegisterTemplateVO {

    private final Long campaignTemplateCode;
    private final String campaignTemplateTitle;
    private final String campaignTemplateContents;
    private final Boolean campaignTemplateFlag;
    private final Long adminCode;
}
