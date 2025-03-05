package intbyte4.learnsmate.refresh_token.repository;

import intbyte4.learnsmate.refresh_token.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // 사용자 코드로 refreshToken 조회
    Optional<RefreshToken> findByUserCode(String userCode);

    // refreshToken으로 해당 사용자 찾기
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    // userCode로 refreshToken 삭제
    void deleteByUserCode(String userCode);
}