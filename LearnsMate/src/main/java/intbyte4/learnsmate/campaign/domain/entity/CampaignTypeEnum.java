package intbyte4.learnsmate.campaign.domain.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CampaignTypeEnum {
    INSTANT("INSTANT"),
    RESERVATION("RESERVATION");

    private final String campaignType;

    CampaignTypeEnum(String campaignType) {
        this.campaignType = campaignType;
    }

    @JsonValue
    public String getType() {
        return campaignType;
    }
}
