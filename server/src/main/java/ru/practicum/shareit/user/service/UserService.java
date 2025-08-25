package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;

public interface UserService {

    UserDto addUser(UserDto userDto);

    UserDto getUser(Long id);

    UserDto updateUser(Long id, UserPatchDto userPatchDto);

    void deleteUser(Long id);


}
