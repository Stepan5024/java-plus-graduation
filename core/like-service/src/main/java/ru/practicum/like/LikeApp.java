package ru.practicum.like;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients(basePackages = "ru.practicum.core.api.client")
public class LikeApp {
    public static void main(String[] args) {
        SpringApplication.run(LikeApp.class, args);
    }
}