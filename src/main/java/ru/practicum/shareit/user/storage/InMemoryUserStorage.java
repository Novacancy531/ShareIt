package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Objects;

@Repository
@Slf4j
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> userStorage;
    private long currentId;

    @Override
    public User addUser(User user) {
        if (userStorage.values().stream()
                .anyMatch(existing -> existing.getEmail().equals(user.getEmail()))) {
            throw new ConditionsNotMetException("Такой email уже используется.");
        }

        User newUser = user.withId(getNewId());
        userStorage.put(newUser.getId(), newUser);

        log.debug("Пользователь {} сохранён в базе данных c id: {}.", newUser.getName(), newUser.getId());

        return newUser;
    }

    @Override
    public User getUser(Long id) {
        log.debug("Получение профиля пользователя с id: {}.", id);
        return userStorage.get(id);
    }

    @Override
    public User updateUser(User user) {
        if (userStorage.values().stream()
                .filter(user1 -> !Objects.equals(user1.getId(), user.getId()))
                .anyMatch(existing -> existing.getEmail().equals(user.getEmail()))) {
            throw new ConditionsNotMetException("Такой email уже используется.");
        }

        userStorage.replace(user.getId(), user);
        log.debug("Обновления профиля пользователя {} с id: {}", user.getName(), user.getId());

        return user;
    }

    @Override
    public void deleteUser(Long id) {
        log.debug("Удаление пользователя с id: {}.", id);
        userStorage.remove(id);
    }

    private long getNewId() {
        return ++currentId;
    }
}
