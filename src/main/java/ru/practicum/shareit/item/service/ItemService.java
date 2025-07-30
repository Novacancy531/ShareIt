package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;

import java.util.Collection;

public interface ItemService {

    public ItemDto addItem(Long userId, ItemDto itemDto);

    public ItemDto getItem(Long id);

    public Collection<ItemDto> getUserItems(Long id);

    public ItemDto updateItem(Long userId, Long itemId, ItemPatchDto itemPatchDto);

    public void deleteItem(Long id);

    public Collection<ItemDto> searchItems(String query);
}

