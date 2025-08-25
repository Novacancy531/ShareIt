package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {

    public static Item mapToItem(ItemDto itemDto) {
        User owner = itemDto.getOwner() != null ? User.builder().id(itemDto.getOwner()).build() : null;

        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(owner)
                .requestId(itemDto.getRequestId())
                .build();
    }

    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner() != null ? item.getOwner().getId() : null)
                .comments(item.getComments() != null
                ? item.getComments().stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList())
                : null)
                .requestId(item.getRequestId())
                .build();
    }

    public static ItemRequestDto mapToItemRequestDto(Item item) {
        return ItemRequestDto.builder()
                .itemId(item.getId())
                .name(item.getName())
                .ownerId(item.getOwner().getId())
                .build();
    }
}
