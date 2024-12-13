package intbyte4.learnsmate.campaign.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class FindAllCampaignsDTO {
    // 캠페인 정보
    private Long campaignCode;
    private String campaignTitle;
    private String campaignContents;
    private String campaignType;
    private String campaignMethod;
    private Boolean campaignSendFlag;
    private LocalDateTime campaignSendDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 로그인한 직원 정보
    private Long adminCode;
    private String adminName;

}
