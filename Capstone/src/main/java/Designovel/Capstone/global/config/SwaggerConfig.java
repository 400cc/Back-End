package Designovel.Capstone.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springCapstoneOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Designovel Capstone API")
                        .description("디자이노블 캡스톤 API 명세서 입니다")
                        .version("v.0.0.1"));
    }
}