package ru.practicum.like.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.like.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDto userToUserDto(User user);

    User userRequestDtoToUser(UserRequestDto userRequestDto);

    List<UserDto> listUserToListUserDto(List<User> users);
}
