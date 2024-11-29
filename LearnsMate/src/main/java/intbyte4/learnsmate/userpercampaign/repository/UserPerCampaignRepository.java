package intbyte4.learnsmate.userpercampaign.repository;

import intbyte4.learnsmate.campaign.domain.entity.Campaign;
import intbyte4.learnsmate.userpercampaign.domain.dto.UserPerCampaignDTO;
import intbyte4.learnsmate.userpercampaign.domain.entity.UserPerCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPerCampaignRepository extends JpaRepository<UserPerCampaign, Integer> {
    List<UserPerCampaign> findByCampaign(Campaign campaign);

    void deleteByCampaign_CampaignCode(Long campaignCode);

    @Query("SELECT new intbyte4.learnsmate.userpercampaign.domain.dto.UserPerCampaignDTO(u.userPerCampaignCode, u.campaign.campaignCode, u.student.memberCode) " +
            "FROM userPerCampaign u WHERE u.campaign.campaignCode = :campaignCode")
    List<UserPerCampaignDTO> findStudentsByCampaignCode(@Param("campaignCode") Long campaignCode);
}
