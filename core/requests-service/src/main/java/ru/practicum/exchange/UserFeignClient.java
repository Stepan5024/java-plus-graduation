package ru.practicum.exchange;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.dto.UserDto;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    @GetMapping("/users/{userId}")
    UserDto getUserById(@PathVariable("userId") long userId);
}