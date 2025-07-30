package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        checkUser(userId);
        return ItemMapper.mapToItemDto(itemStorage.addItem(userId, ItemMapper.mapToItem(itemDto)));
    }

    @Override
    public ItemDto getItem(Long id) {
        return ItemMapper.mapToItemDto(itemStorage.getItem(id));
    }

    @Override
    public Collection<ItemDto> getUserItems(Long id) {
        return itemStorage.getUserItems(id).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemPatchDto itemPatchDto) {
        Item item = itemStorage.getItem(itemId);
        if (item == null) {
            throw new NotFoundException("Предмет для обновления не найден.");
        }
        if (!Objects.equals(userId, item.getOwner())) {
            throw new NotFoundException("Не найден предмет для редактирования.");
        }

        item = item.toBuilder().name(itemPatchDto.name() != null ? itemPatchDto.name() : item.getName())
                .description(itemPatchDto.description() != null ? itemPatchDto.description() : item.getDescription())
                .available(itemPatchDto.available() != null ? itemPatchDto.available() : item.getAvailable())
                .build();

        itemStorage.updateItem(item);

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public void deleteItem(Long id) {
        itemStorage.deleteItem(id);
    }

    @Override
    public Collection<ItemDto> searchItems(String query) {
        return itemStorage.searchItems(query).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    private void checkUser(Long userId) {
        userService.getUser(userId);
    }
}
