package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface ItemService {

    ItemDto addItem(Long userId, ItemDto itemDto);

    ItemDto getItem(Long id);

    Collection<ItemDto> getUserItems(Long id);

    ItemDto updateItem(Long userId, Long itemId, ItemPatchDto itemPatchDto);

    void deleteItem(Long id);

    Collection<ItemDto> searchItems(String query);
}

