package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;

public interface UserService {

    public UserDto addUser(UserDto userDto);

    public UserDto getUser(Long id);

    public UserDto updateUser(Long id, UserPatchDto userPatchDto);

    public void deleteUser(Long id);
}
