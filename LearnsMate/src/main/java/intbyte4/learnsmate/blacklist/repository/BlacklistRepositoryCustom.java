package intbyte4.learnsmate.blacklist.repository;

import intbyte4.learnsmate.blacklist.domain.dto.BlacklistFilterRequestDTO;
import intbyte4.learnsmate.blacklist.domain.entity.Blacklist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlacklistRepositoryCustom {
    Page<Blacklist> searchBy(BlacklistFilterRequestDTO request, Pageable pageable);
}
