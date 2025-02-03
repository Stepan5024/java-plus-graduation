package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable long userId) {
        log.info("|| ==> GET Getting user by id: {}", userId);
        UserDto userDto = userService.getById(userId);
        log.info("|| <== GET Returning user: {}", userDto);
        return userDto;
    }

    @GetMapping("/{userId}/check")
    public boolean checkExistence(@PathVariable("userId") long userId) {
        log.info("==> GET. Checking exist for User: {}", userId);
        boolean result = userService.checkExistence(userId);
        log.info("|==| GET. User exist: {}", result);
        return result;

    }

    @GetMapping("/all")
    public Map<Long, UserDto> getAll(@RequestParam List<Long> userIds) {
        log.info("==> GET. Getting users by ids: {}", userIds);
        Map<Long, UserDto> users = userService.getByIds(userIds);
        log.info("<== GET. Returning users by ids: {}", userIds);
        return users;
    }

}
