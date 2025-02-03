package ru.practicum.event.config;

import feign.Feign;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConfig {
    @Value("${service.name}")
    private String appName;

    @Bean
    public Feign.Builder feignBuilder() {

        return Feign.builder();
    }
}
