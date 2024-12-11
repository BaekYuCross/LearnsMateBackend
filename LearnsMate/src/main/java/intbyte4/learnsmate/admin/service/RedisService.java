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

    public boolean deleteToken(String userCode) {
        if (userCode == null || userCode.isEmpty()) {
            log.warn("The userCode is null or empty in the delete request.");
            return false;
        }

        String key = "refreshToken:" + userCode;
        try {
            Boolean isDeleted = redisTemplate.delete(key);
            if (Boolean.TRUE.equals(isDeleted)) {
                log.info("Successfully deleted the refreshToken from Redis: {}", key);
                return true;
            } else {
                log.warn("Failed to delete the refreshToken from Redis or the key does not exist: {}", key);
                log.info("Current keys in Redis: {}", redisTemplate.keys("refreshToken:*"));
                return false;
            }
        } catch (Exception e) {
            log.error("An exception occurred while deleting the refreshToken from Redis: {}", e.getMessage(), e);
            return false;
        }
    }
}

