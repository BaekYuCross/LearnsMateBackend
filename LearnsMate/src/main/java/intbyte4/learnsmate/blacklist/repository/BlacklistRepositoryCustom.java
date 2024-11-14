package intbyte4.learnsmate.blacklist.repository;

import intbyte4.learnsmate.blacklist.domain.dto.BlacklistFilterRequestDTO;
import intbyte4.learnsmate.blacklist.domain.entity.Blacklist;

import java.util.List;

public interface BlacklistRepositoryCustom {
    List<Blacklist> searchBy(BlacklistFilterRequestDTO request);
}
