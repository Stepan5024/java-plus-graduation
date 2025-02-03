package ru.practicum.like.exchange;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.like.dto.UserRequestDto;

@FeignClient(name = "user-service")
public interface UserFeignClient {
    @GetMapping("/users/{userId}")
    UserRequestDto getUserById(@PathVariable long userId);
}