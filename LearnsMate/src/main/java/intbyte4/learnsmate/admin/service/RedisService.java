package intbyte4.learnsmate.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void deleteToken(String refreshToken) {
        if (refreshToken != null && !refreshToken.isEmpty()) {
            redisTemplate.delete("refreshToken:" + refreshToken); // Redis에서 삭제
        }
    }
}
