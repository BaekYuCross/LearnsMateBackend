package intbyte4.learnsmate.userpercampaign.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class UserPerCampaignDTO {
    private Long userPerCampaignCode;
    private Long campaignCode;
    private Long studentCode;
}
