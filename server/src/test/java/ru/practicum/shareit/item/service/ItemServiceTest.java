package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookingService bookingService;

    private Item item;
    private User user;
    private UserDto userDto;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "Aleksandr", "Dolsa.broadstaff@gmail.com");
        userDto = UserMapper.mapToUserDto(user);
        item = Item.builder()
                .id(1L)
                .name("Дрель")
                .description("Норм дрель")
                .available(true)
                .owner(user)
                .build();
        itemDto = ItemDto.builder()
                .id(null)
                .name("Дрель")
                .description("Норм дрель")
                .available(true)
                .owner(null)
                .requestId(null)
                .build();
    }

    @Test
    void addItem() {
        when(userService.getUser(1L)).thenReturn(userDto);
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto result = itemService.addItem(1L, itemDto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Дрель");
        verify(itemRepository).save(any());
    }

    @Test
    void getItem() {
        when(itemRepository.findByIdWithCommentsAndAuthors(1L)).thenReturn(Optional.of(item));

        ItemDto result = itemService.getItem(1L);

        assertThat(result.getName()).isEqualTo("Дрель");
    }

    @Test
    void updateItem() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        ItemPatchDto patch = new ItemPatchDto(1L, "Молоток", null, null);

        ItemDto updated = itemService.updateItem(1L, 1L, patch);

        assertThat(updated.getName()).isEqualTo("Молоток");
        assertThat(updated.getDescription()).isEqualTo("Норм дрель");
        assertThat(updated.getAvailable()).isTrue();
        verify(itemRepository).save(any());
    }

    @Test
    void deleteItem() {
        itemService.deleteItem(1L);
        verify(itemRepository).deleteById(1L);
    }

    @Test
    void searchItems() {
        when(itemRepository.searchAvailableItems("Дрель")).thenReturn(List.of(item));

        var result = itemService.searchItems("Дрель");

        assertThat(result).hasSize(1);
    }

    @Test
    void createComment() {
        Booking booking = Booking.builder()
                .status(Status.APPROVED)
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .build();

        when(bookingService.findByBookerIdAndItemId(1L, 1L)).thenReturn(booking);
        when(userService.getUser(1L)).thenReturn(userDto);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Comment comment = Comment.builder().id(1L).comment("Норм").author(user).item(item).build();
        when(commentRepository.save(any())).thenReturn(comment);

        CommentDto result = itemService.createComment(1L, 1L, new CommentDto(null, "Норм", null, null));

        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void getItemsByRequestId_ShouldReturnItems() {
        when(itemRepository.findItemsByRequestId(1L)).thenReturn(List.of(item));

        var result = itemService.getItemsByRequestId(1L);

        assertThat(result).hasSize(1);
    }
}
