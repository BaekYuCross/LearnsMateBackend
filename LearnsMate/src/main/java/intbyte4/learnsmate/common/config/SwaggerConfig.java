package intbyte4.learnsmate.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/* http://localhost:8080/swagger-ui/index.html */
@OpenAPIDefinition(
        info = @Info(title = "LearnsMate API 명세서",
                description = "LearnsMate 기능별 API 명세서"))
@Configuration
public class SwaggerConfig {

}
