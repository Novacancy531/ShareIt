package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.storage.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден."));

        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwner(userId);

        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItem(Long id) {
        return ItemMapper.mapToItemDto(itemRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Предмет не найден.")));
    }

    @Override
    public Collection<ItemDto> getUserItems(Long id) {
        return itemRepository.findByOwner(id).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemPatchDto itemPatchDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(()
                -> new NotFoundException("Предмет для обновления не найден."));

        if (!Objects.equals(userId, item.getOwner())) {
            throw new NotFoundException("Не найден предмет для редактирования.");
        }

        item = item.toBuilder().name(itemPatchDto.name() != null ? itemPatchDto.name() : item.getName())
                .description(itemPatchDto.description() != null ? itemPatchDto.description() : item.getDescription())
                .available(itemPatchDto.available() != null ? itemPatchDto.available() : item.getAvailable())
                .build();

        itemRepository.save(item);

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public Collection<ItemDto> searchItems(String query) {

        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }

        return itemRepository.searchAvailableItems(query).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }
}
