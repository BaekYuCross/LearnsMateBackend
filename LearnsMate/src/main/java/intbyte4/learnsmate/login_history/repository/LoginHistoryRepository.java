package intbyte4.learnsmate.login_history.repository;

import intbyte4.learnsmate.login_history.domain.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

    List<LoginHistory> findByMember_MemberCode(Long memberCode);
}
