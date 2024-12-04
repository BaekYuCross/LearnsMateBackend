package intbyte4.learnsmate.common.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        String redisHost = environment.getProperty("spring.data.redis.host");
        int redisPort = environment.getProperty("spring.data.redis.port", Integer.class, 6379);

        log.info("Creating Redis connection to {}:{}", redisHost, redisPort);

        LettuceConnectionFactory factory = new LettuceConnectionFactory();
        factory.setHostName(redisHost);
        factory.setPort(redisPort);
        return factory;
    }

    /*
     * RedisConnection 에서 넘겨준 byte 값을 직렬화 RedisTemplate 은
     * Redis 데이터를 저장하고 조회하는 기능을 하는 클래스 REdis cli 를 사용해 Redis 데이터를 직접 조회할때,
     * Redis 데이터를 문자열로 반환하기 위한 설정
     */

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @PostConstruct
    public void init() {
        log.info("Redis Host: {}", environment.getProperty("spring.data.redis.host"));
    }
}