package intbyte4.learnsmate.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class CookieConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setSameSite("None"); // SameSite=None 설정
        serializer.setUseSecureCookie(true); // Secure 쿠키 설정
        serializer.setDomainName("learnsmate.shop"); // 쿠키 도메인 설정
        serializer.setCookiePath("/"); // 경로 설정
        return serializer;
    }
}
