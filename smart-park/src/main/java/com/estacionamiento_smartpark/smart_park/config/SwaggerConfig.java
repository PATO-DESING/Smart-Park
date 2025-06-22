package com.estacionamiento_smartpark.smart_park.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI().info(
            new Info()
            .title("Api administración de estacionamiento")
            .version("1.1")
            .description("Con esta API se puede administrar los estacionamientos.")
        );
    }


}
