package com.splitwise.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow all routes
                .allowedOrigins("http://127.0.0.1:5500") // frontend origin
                .allowedMethods("*") // Allow all methods: GET, POST, etc.
                .allowedHeaders("*");
    }
}