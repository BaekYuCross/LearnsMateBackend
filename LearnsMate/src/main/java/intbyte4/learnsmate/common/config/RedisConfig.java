package intbyte4.learnsmate.common.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
@Slf4j
public class RedisConfig {

    private final Environment environment;

    public RedisConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        String redisHost = environment.getProperty("spring.data.redis.host", "localhost");
        int redisPort = environment.getProperty("spring.data.redis.port", Integer.class, 6379);
        String redisPassword = environment.getProperty("spring.data.redis.password", "");

        log.info("Connecting to Redis at {}:{}", redisHost, redisPort);

        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration(redisHost, redisPort);

        if (!redisPassword.isEmpty()) {
            standaloneConfig.setPassword(RedisPassword.of(redisPassword));
        }

        LettuceConnectionFactory factory = new LettuceConnectionFactory(standaloneConfig);
        factory.afterPropertiesSet(); // 설정 적용

        // Redis 연결 테스트 로깅
        try {
            factory.getConnection().ping();
            log.info("✅ Redis 연결 성공!");
        } catch (Exception e) {
            log.error("❌ Redis 연결 실패: {}", e.getMessage());
        }

        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // ObjectMapper 설정 (타입 정보를 포함하도록 설정)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL, // 모든 객체 타입 정보 포함
                JsonTypeInfo.As.PROPERTY
        );

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        redisTemplate.setKeySerializer(new StringRedisSerializer());  // 문자열 키 사용
        redisTemplate.setValueSerializer(serializer);  // Value Serializer 설정
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
