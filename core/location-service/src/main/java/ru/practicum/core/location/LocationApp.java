package ru.practicum.core.location;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.practicum.core.api.client.LikeServiceClient;
import ru.practicum.core.api.client.UserServiceClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(clients = {UserServiceClient.class, LikeServiceClient.class})
public class LocationApp {
    public static void main(String[] args) {
        SpringApplication.run(LocationApp.class, args);
    }
}
