package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserRequestDto;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserDto> getAllUsers(List<Long> ids, int from, int size);

    List<UserDto> getAllUsersBySortRating(int from, int size);

    UserDto createUser(UserRequestDto requestDto);

    UserDto getById(long userId);

    boolean checkExistence(long userId);

    Map<Long, UserDto> getByIds(List<Long> userIds);

    void deleteUser(long userId);
}
