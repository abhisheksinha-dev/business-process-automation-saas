package com.abhishek.bpa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Business Process Automation SaaS API")
                        .description("Multi-tenant Business Process Automation backend APIs")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Abhishek Sinha")
                                .email("techabhi2000@gmail.com")
                        )

                );
    }
}
