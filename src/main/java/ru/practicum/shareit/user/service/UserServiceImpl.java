package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto addUser(UserDto userDto) {
        return UserMapper.mapToUserDto(userStorage.addUser(UserMapper.mapToUser(userDto)));
    }

    @Override
    public UserDto getUser(Long id) {
        User user = userStorage.getUser(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + id + " не найден.");
        }
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserPatchDto userPatchDto) {
        User user = userStorage.getUser(id);
        User updatedUser = User.builder()
                .id(user.getId())
                .name(userPatchDto.name() != null ? userPatchDto.name() : user.getName())
                .email(userPatchDto.email() != null ? userPatchDto.email() : user.getEmail())
                .build();

        return UserMapper.mapToUserDto(userStorage.updateUser(updatedUser));
    }

    @Override
    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }
}
