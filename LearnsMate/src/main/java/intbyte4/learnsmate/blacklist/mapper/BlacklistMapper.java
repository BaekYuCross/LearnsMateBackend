package intbyte4.learnsmate.blacklist.mapper;

import intbyte4.learnsmate.blacklist.domain.dto.BlacklistDTO;
import intbyte4.learnsmate.blacklist.domain.entity.Blacklist;
import intbyte4.learnsmate.blacklist.domain.vo.response.ResponseFindReportVO;
import org.springframework.stereotype.Component;

@Component
public class BlacklistMapper {


    public BlacklistDTO fromBlacklistToBlacklistDTO(Blacklist blacklist) {
        return BlacklistDTO.builder()
                .blackCode(blacklist.getBlackCode())
                .blackReason(blacklist.getBlackReason())
                .createdAt(blacklist.getCreatedAt())
                .updatedAt(blacklist.getUpdatedAt())
                .memberCode(blacklist.getMember().getMemberCode())
                .reportCode(blacklist.getReport().getReportCode())
                .adminCode(blacklist.getAdmin().getAdminCode())
                .build();
    }

    public ResponseFindReportVO fromBlacklistDTOToResponseFindReportVO(BlacklistDTO blacklistDTO) {
        return ResponseFindReportVO.builder()
                .blackCode(blacklistDTO.getBlackCode())
                .blackReason(blacklistDTO.getBlackReason())
                .createdAt(blacklistDTO.getCreatedAt())
                .updatedAt(blacklistDTO.getUpdatedAt())
                .memberCode(blacklistDTO.getMemberCode())
                .reportCode(blacklistDTO.getReportCode())
                .adminCode(blacklistDTO.getAdminCode())
                .build();
    }
}
