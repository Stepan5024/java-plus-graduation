package ru.practicum.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.UserRequestDto;
import ru.practicum.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDto userToUserDto(User user);

    User userRequestDtoToUser(UserRequestDto userRequestDto);

    List<UserDto> listUserToListUserDto(List<User> users);
}
