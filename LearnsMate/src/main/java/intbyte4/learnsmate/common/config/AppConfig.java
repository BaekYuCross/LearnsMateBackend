package intbyte4.learnsmate.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
//    @Bean
//    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    @Bean
    public RestTemplate restTemplate() { return new RestTemplate(); }
}
