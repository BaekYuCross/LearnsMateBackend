package intbyte4.learnsmate.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void deleteToken(String userCode) {
        if (userCode == null || userCode.isEmpty()) {
            log.warn("삭제 요청 시 userCode가 null이거나 비어 있습니다.");
            return;
        }

        String key = "refreshToken:" + userCode; // Redis 키 형식 설정
        try {
            Boolean isDeleted = redisTemplate.delete(key);
            if (Boolean.TRUE.equals(isDeleted)) {
                log.info("Redis에서 refreshToken 삭제 성공: {}", key);
            } else {
                log.warn("Redis에서 refreshToken 삭제 실패 또는 키가 존재하지 않음: {}", key);
            }
        } catch (Exception e) {
            log.error("Redis에서 refreshToken 삭제 중 예외 발생: {}", e.getMessage(), e);
        }
    }
}

