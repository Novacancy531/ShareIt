package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemStorage {
    private final HashMap<Long, Item> itemStorage;
    private long currentId;

    @Override
    public Item addItem(Long userId, Item item) {
        Item newItem = item.toBuilder().id(getNewId()).owner(userId).build();
        itemStorage.put(newItem.getId(), newItem);

        log.debug("Добавлен предмет {} с id: {}.", item.getName(), item.getId());

        return newItem;
    }

    @Override
    public Item getItem(Long id) {
        log.debug("Получение предмета с id: {}.", id);
        return itemStorage.get(id);
    }

    @Override
    public Collection<Item> getUserItems(Long userId) {
        log.debug("Получение всех предметов пользователя с id: {}", userId);
        return itemStorage.values().stream()
                .filter(item -> Objects.equals(item.getOwner(), userId))
                .toList();
    }

    @Override
    public Item updateItem(Item item) {
        log.debug("Обновление информации о предмете {} с id: {}.", item.getName(), item.getId());
        itemStorage.replace(item.getId(), item);
        return item;
    }

    @Override
    public void deleteItem(Long id) {
        log.debug("Удаления предмета с id: {}", id);
        itemStorage.remove(id);
    }

    @Override
    public Collection<Item> searchItems(String query) {
        if (query == null || query.isBlank()) {
            log.debug("Получен пустой запрос.");
            return List.of();
        }

        String search = query.toLowerCase();

        log.debug("Поиск предметов содержащих: {}", query);

        return itemStorage.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(search)
                        || item.getDescription().toLowerCase().contains(search))
                .filter(Item::getAvailable)
                .toList();
    }

    private long getNewId() {
        return ++currentId;
    }
}
