package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {

    Item addItem(Long userId, Item item);

    Item getItem(Long id);

    Collection<Item> getUserItems(Long id);

    Item updateItem(Item item);

    void deleteItem(Long id);

    Collection<Item> searchItems(String query);
}

