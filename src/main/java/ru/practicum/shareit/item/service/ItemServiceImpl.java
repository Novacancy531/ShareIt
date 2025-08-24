package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final BookingService bookingService;

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        User user = UserMapper.mapToUser(userService.getUser(userId));

        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwner(user);

        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public ItemDto getItem(Long id) {
        Item item = itemRepository.findByIdWithCommentsAndAuthors(id)
                .orElseThrow(() -> new NotFoundException("Предмет не найден."));
        return ItemMapper.mapToItemDto(item);
    }

    public Collection<ItemDto> getUserItems(Long ownerId) {
        List<Item> items = itemRepository.findByOwnerId(ownerId);

        List<Booking> bookings = bookingService.findAllByItemIdIn(
                items.stream().map(Item::getId).toList()
        );

        LocalDateTime now = LocalDateTime.now();

        Map<Long, List<Booking>> bookingsByItem = bookings.stream()
                .collect(Collectors.groupingBy(b -> b.getItem().getId()));

        return items.stream()
                .map(item -> {
                    List<Booking> itemBookings = bookingsByItem.getOrDefault(item.getId(), List.of());

                    return ItemMapper.mapToItemDto(item).toBuilder()
                            .lastBooking(itemBookings.stream()
                                    .filter(b -> b.getEnd().isBefore(now))
                                    .max(Comparator.comparing(Booking::getEnd))
                                    .map(Booking::getEnd)
                                    .orElse(null))
                            .nextBooking(itemBookings.stream()
                                    .filter(b -> b.getStart().isAfter(now))
                                    .min(Comparator.comparing(Booking::getStart))
                                    .map(Booking::getStart)
                                    .orElse(null))
                            .build();
                })
                .toList();
    }


    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemPatchDto itemPatchDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(()
                -> new NotFoundException("Предмет для обновления не найден."));

        if (!Objects.equals(userId, item.getOwner().getId())) {
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

    @Override
    public CommentDto createComment(Long userId, Long itemId, CommentDto commentDto) {

        Booking booking = bookingService.findByBookerIdAndItemId(userId, itemId);

        if (!booking.getStatus().equals(Status.APPROVED) || LocalDateTime.now().isBefore(booking.getEnd())) {
            throw new AccessDeniedException("Вы не можете оставить комментарий");
        }

        var user = UserMapper.mapToUser(userService.getUser(userId));
        var item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет отсутствует"));

        Comment comment = Comment.builder()
                .author(user)
                .item(item)
                .comment(commentDto.text())
                .createdTime(LocalDateTime.now())
                .build();

        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }
}
