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

@Configuration
@Slf4j
public class RedisConfig {

    private final Environment environment;

    public RedisConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // 구성 엔드포인트 읽기
        String redisClusterEndpoint = environment.getProperty("spring.data.redis.cluster.endpoint");
        int redisPort = environment.getProperty("spring.data.redis.port", Integer.class, 6379);

        if (redisClusterEndpoint == null || redisClusterEndpoint.isEmpty()) {
            throw new IllegalArgumentException("Redis cluster endpoint must not be null or empty");
        }

        log.info("Connecting to Redis cluster at: {}:{}", redisClusterEndpoint, redisPort);

        // Redis 클러스터 구성
        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();
        clusterConfig.clusterNode(redisClusterEndpoint, redisPort);
        clusterConfig.setMaxRedirects(3);

        return new LettuceConnectionFactory(clusterConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    @PostConstruct
    public void init() {
        log.info("Redis configuration initialized");
    }
}
