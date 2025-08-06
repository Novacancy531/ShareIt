package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

public interface UserStorage {

    User addUser(User user);

    User getUser(Long id);

    User updateUser(User user);

    void deleteUser(Long id);
}
