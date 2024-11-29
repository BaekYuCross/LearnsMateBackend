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
        if (userCode != null && !userCode.isEmpty()) {
            String key = "refreshToken:" + userCode; // 키 이름 형식에 맞게 설정
            Boolean isDeleted = redisTemplate.delete(key);
            if (isDeleted != null && isDeleted) {
                log.info("Redis에서 refreshToken 삭제 성공: {}", key);
            } else {
                log.warn("Redis에서 refreshToken 삭제 실패 또는 존재하지 않음: {}", key);
            }
        }
    }

}
