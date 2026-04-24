package com.telusko.springecom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Applies CORS to every endpoint in your app
                        .allowedOrigins("http://localhost:5174") // YOUR exact React URL from the error
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // OPTIONS is crucial for preflight
                        .allowedHeaders("*") // Allows Authorization and other headers
                        .allowCredentials(true); // Required if you use cookies or auth tokens
            }
        };
    }
}