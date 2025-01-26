package ru.practicum.exchange;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.dto.user.UserDto;
import ru.practicum.model.User;

@FeignClient(name = "user-service")
public interface UserFeignClient {
    @GetMapping("/users/{id}")
    UserDto getUserById(@PathVariable Long id);

    @GetMapping("/users/{id}/exists")
    Boolean existsById(@PathVariable Long id);
}