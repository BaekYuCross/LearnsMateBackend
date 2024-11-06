package intbyte4.learnsmate.blacklist.service;

import intbyte4.learnsmate.blacklist.domain.dto.BlacklistDTO;
import intbyte4.learnsmate.blacklist.domain.entity.Blacklist;
import intbyte4.learnsmate.blacklist.mapper.BlacklistMapper;
import intbyte4.learnsmate.blacklist.repository.BlacklistRepository;
import intbyte4.learnsmate.member.domain.MemberType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlacklistService {

    private final BlacklistRepository blacklistRepository;
    private final BlacklistMapper blacklistMapper;

    @Autowired
    public BlacklistService(BlacklistRepository blacklistRepository, BlacklistMapper blacklistMapper) {
        this.blacklistRepository = blacklistRepository;
        this.blacklistMapper = blacklistMapper;
    }

    // 1. flag는 볼필요 없음. -> 학생, 강사만 구분해야함.
    public List<BlacklistDTO> findAllBlacklistByMemberType(MemberType memberType) {

        List<Blacklist> blacklistList = blacklistRepository.findAllBlacklistByMemberType(memberType);

        List<BlacklistDTO> blacklistDTOList = new ArrayList<>();

//                .memberCode(blacklist.getMemberCode())
//                .reportCode(blacklist.getReportCode())
//                .adminCode(blacklist.getAdminCode())

        for (Blacklist blacklist : blacklistList) {
            // Blacklsit -> BlacklsitDTO
            blacklistDTOList.add(blacklistMapper.fromBlacklistToBlacklistDTO(blacklist));
        }

        return blacklistDTOList;
    }
}
