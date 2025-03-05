package intbyte4.learnsmate.refresh_token.service;

import intbyte4.learnsmate.refresh_token.domain.entity.RefreshToken;
import intbyte4.learnsmate.refresh_token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // DBMS에 refreshToken 저장
    public void saveRefreshTokenToDB(String userCode, String refreshToken) {
        long startTime = Instant.now().toEpochMilli();  // 저장 시작 시간 기록

        RefreshToken token = RefreshToken.builder()
                .userCode(userCode)
                .refreshToken(refreshToken)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        refreshTokenRepository.save(token);
    }

    // DBMS에서 refreshToken 조회
    public String getRefreshTokenFromDB(String userCode) {
        long startTime = Instant.now().toEpochMilli();  // 조회 시작 시간 기록

        Optional<RefreshToken> tokenOptional = refreshTokenRepository.findByUserCode(userCode);
        String refreshToken = tokenOptional.map(RefreshToken::getRefreshToken).orElse(null);

        long endTime = Instant.now().toEpochMilli();  // 조회 끝 시간 기록
        long duration = endTime - startTime;  // 걸린 시간 계산
        System.out.println("DBMS 조회 시간: " + duration + "ms");

        return refreshToken;
    }

    // DBMS에서 refreshToken 삭제
    public void deleteRefreshTokenFromDB(String userCode) {
        long startTime = Instant.now().toEpochMilli();  // 삭제 시작 시간 기록

        refreshTokenRepository.deleteByUserCode(userCode);
    }
}