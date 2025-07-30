package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

public interface UserStorage {

    public User addUser(User user);

    public User getUser(Long id);

    public User updateUser(User user);

    public void deleteUser(Long id);
}
