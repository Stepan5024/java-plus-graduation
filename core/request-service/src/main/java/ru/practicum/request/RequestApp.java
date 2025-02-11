package ru.practicum.request;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.practicum.core.api.client.EventServiceClient;
import ru.practicum.core.api.client.UserServiceClient;

@SpringBootApplication
@EnableFeignClients(clients = {EventServiceClient.class, UserServiceClient.class})
public class RequestApp {
    public static void main(String[] args) {
        SpringApplication.run(RequestApp.class, args);
    }
}
