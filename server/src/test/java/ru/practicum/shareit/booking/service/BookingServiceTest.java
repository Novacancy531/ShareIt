package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User owner;
    private User booker;
    private UserDto ownerDto;
    private UserDto bookerDto;
    private Item item;
    private Booking booking;
    private BookingCreateDto bookingCreateDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        owner = User.builder()
                .id(1L)
                .name("Aleksandr")
                .email("Dolsa.broadstaff@gmail.com")
                .build();

        booker = User.builder()
                .id(2L)
                .name("Aleksandr")
                .email("Dolsa.broadstafF@gmail.com")
                .build();

        ownerDto = UserMapper.mapToUserDto(owner);
        bookerDto = UserMapper.mapToUserDto(booker);

        item = Item.builder()
                .id(10L)
                .name("Дрель")
                .description("Супер дрель")
                .available(true)
                .owner(owner)
                .build();

        bookingCreateDto = new BookingCreateDto(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item.getId()
        );

        booking = Booking.builder()
                .id(100L)
                .item(item)
                .booker(booker)
                .start(bookingCreateDto.start())
                .end(bookingCreateDto.end())
                .status(Status.WAITING)
                .build();
    }

    @Test
    void createBooking() {
        when(userService.getUser(booker.getId())).thenReturn(bookerDto);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto dto = bookingService.createBooking(booker.getId(), bookingCreateDto);

        assertNotNull(dto);
        assertEquals(Status.WAITING, dto.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void createBookingNotFound() {
        when(userService.getUser(booker.getId())).thenReturn(bookerDto);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(booker.getId(), bookingCreateDto));
    }

    @Test
    void createBookingNotAvailable() {
        item.setAvailable(false);
        when(userService.getUser(booker.getId())).thenReturn(bookerDto);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        assertThrows(AccessDeniedException.class,
                () -> bookingService.createBooking(booker.getId(), bookingCreateDto));
    }

    @Test
    void approvedBooking() {
        booking.setBooker(booker);
        booking.getItem().setOwner(owner);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        BookingDto approved = bookingService.approvedBooking(owner.getId(), booking.getId(), true);

        assertEquals(Status.APPROVED, approved.getStatus());
        BookingDto rejected = bookingService.approvedBooking(owner.getId(), booking.getId(), false);
        assertEquals(Status.REJECTED, rejected.getStatus());
    }

    @Test
    void approvedBookingNotFound() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.approvedBooking(owner.getId(), booking.getId(), true));
    }

    @Test
    void getBooking_success() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        BookingDto dtoForOwner = bookingService.getBooking(owner.getId(), booking.getId());
        assertNotNull(dtoForOwner);

        BookingDto dtoForBooker = bookingService.getBooking(booker.getId(), booking.getId());
        assertNotNull(dtoForBooker);
    }

    @Test
    void getBookingNotFound() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.getBooking(owner.getId(), booking.getId()));
    }

    @Test
    void getBookingNotOwner() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        User other = User.builder().id(777L).name("Aleksandr").email("Dolsa.broadstaff@gmail.com").build();

        assertThrows(AccessDeniedException.class,
                () -> bookingService.getBooking(other.getId(), booking.getId()));
    }

    @Test
    void getBookings() {
        when(userService.getUser(booker.getId())).thenReturn(bookerDto);
        when(bookingRepository.findBookingsByUserAndState(booker.getId(), false, "ALL"))
                .thenReturn(List.of(booking));

        var list = bookingService.getBookings(booker.getId(), "ALL");

        assertEquals(1, list.size());
        verify(userService).getUser(booker.getId());
    }

    @Test
    void getBookingsForOwner() {
        when(userService.getUser(owner.getId())).thenReturn(ownerDto);
        when(bookingRepository.findBookingsByUserAndState(owner.getId(), true, "ALL"))
                .thenReturn(List.of(booking));

        var list = bookingService.getBookingsForOwner(owner.getId(), "ALL");

        assertEquals(1, list.size());
        verify(userService).getUser(owner.getId());
    }

    @Test
    void findAllByItemIdIn() {
        when(bookingRepository.findAllByItemIdIn(List.of(10L))).thenReturn(List.of(booking));

        var result = bookingService.findAllByItemIdIn(List.of(10L));
        assertEquals(1, result.size());
    }

    @Test
    void findByBookerIdAndItemId() {
        when(bookingRepository.findByBookerIdAndItemId(booker.getId(), item.getId())).thenReturn(booking);

        var result = bookingService.findByBookerIdAndItemId(booker.getId(), item.getId());
        assertSame(booking, result);
    }
}
