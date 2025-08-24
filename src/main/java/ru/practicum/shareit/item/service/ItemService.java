package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemService {

    ItemDto addItem(Long userId, ItemDto itemDto);

    ItemDto getItem(Long id);

    Collection<ItemDto> getUserItems(Long id);

    ItemDto updateItem(Long userId, Long itemId, ItemPatchDto itemPatchDto);

    void deleteItem(Long id);

    Collection<ItemDto> searchItems(String query);

    CommentDto createComment(Long userId, Long itemId, CommentDto commentDto);

    List<Item> getItemsByRequestId(Long requestId);
}

