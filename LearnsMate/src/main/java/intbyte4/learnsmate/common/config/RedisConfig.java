package intbyte4.learnsmate.common.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfig {

    private final Environment environment;

    public RedisConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        String redisClusterNodes = environment.getProperty("spring.data.redis.cluster.nodes");

        if (redisClusterNodes == null || redisClusterNodes.isEmpty()) {
            throw new IllegalArgumentException("Redis cluster nodes must not be null or empty");
        }

        log.info("Connecting to Redis cluster at: {}", redisClusterNodes);

        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();
        String[] parts = redisClusterNodes.split(":");
        clusterConfig.clusterNode(parts[0], Integer.parseInt(parts[1]));
        clusterConfig.setMaxRedirects(3);

        return new LettuceConnectionFactory(clusterConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
