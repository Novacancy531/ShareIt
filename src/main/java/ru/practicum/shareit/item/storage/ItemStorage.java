package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {

    public Item addItem(Long userId, Item item);

    public Item getItem(Long id);

    public Collection<Item> getUserItems(Long id);

    public Item updateItem(Item item);

    public void deleteItem(Long id);

    public Collection<Item> searchItems(String query);
}

