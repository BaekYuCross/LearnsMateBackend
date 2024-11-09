package intbyte4.learnsmate.campaign.domain.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import intbyte4.learnsmate.member.domain.MemberType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestFindCampaignStudentVO {
    @JsonProperty("member_code")
    private Long memberCode;

    @JsonProperty("member_type")
    private MemberType memberType;

    @JsonProperty("member_email")
    private String memberEmail;

    @JsonProperty("member_password")
    private String memberPassword;

    @JsonProperty("member_name")
    private String memberName;

    @JsonProperty("member_age")
    private Integer memberAge;

    @JsonProperty("member_phone")
    private String memberPhone;

    @JsonProperty("member_address")
    private String memberAddress;

    @JsonProperty("member_birth")
    private LocalDateTime memberBirth;

    @JsonProperty("member_flag")
    private Boolean memberFlag;

    @JsonProperty("member_dormant_status")
    private Boolean memberDormantStatus;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
