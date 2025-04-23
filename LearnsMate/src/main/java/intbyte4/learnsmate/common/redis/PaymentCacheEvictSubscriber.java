package intbyte4.learnsmate.common.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCacheEvictSubscriber implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String payload = new String(message.getBody());

        log.info("Redis Pub/Sub 수신됨 - 채널: {}, 메시지: {}", channel, payload);

        if (!"payment-cache-invalidate".equals(channel)) return;

        Set<Object> rawKeys = redisTemplate.opsForSet().members("payment-cache-keys");
        if (rawKeys == null || rawKeys.isEmpty()) {
            log.info("ℹ삭제할 캐시 키 없음 (payment-cache-keys 비어있음)");
            return;
        }

        Set<String> keys = rawKeys.stream()
                .map(Object::toString)
                .collect(Collectors.toSet());

        redisTemplate.delete(keys); // bulk delete
        redisTemplate.delete("payment-cache-keys"); // set 자체도 삭제

        log.info("{}개 캐시 키 삭제 완료", keys.size());
    }
}