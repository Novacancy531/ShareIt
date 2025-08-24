package ru.practicum.shareit.booking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public BookingDto createBooking(long userId, BookingCreateDto bookingCreateDto) {
        var booker = UserMapper.mapToUser(userService.getUser(userId));

        var item = itemRepository.findById(bookingCreateDto.itemId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));

        if (!item.getAvailable()) {
            throw new AccessDeniedException("Предмет недоступен.");
        }

        Booking booking = BookingMapper.mapToBooking(bookingCreateDto, booker, item);
        booking.setStatus(Status.WAITING);

        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public BookingDto approvedBooking(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (userId != booking.getItem().getOwner().getId()) {
            throw new AccessDeniedException("Нет доступа к изменению статуса");
        }

        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);

        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public BookingDto getBooking(long userId, long bookingId) {
        var booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        var isOwner = userId == booking.getItem().getOwner().getId();
        var isBooker = userId == booking.getBooker().getId();

        if (!isOwner && !isBooker) {
            throw new AccessDeniedException("Нет доступа к просмотру бронирования");
        }

        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookings(Long userId, String state) {
        userService.getUser(userId);

        return bookingRepository.findBookingsByUserAndState(userId, false, state)
                .stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }

    @Override
    public List<BookingDto> getBookingsForOwner(Long userId, String state) {
        userService.getUser(userId);

        return bookingRepository.findBookingsByUserAndState(userId, true, state)
                .stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }

    @Override
    public List<Booking> findAllByItemIdIn(Collection<Long> itemIds) {
        return bookingRepository.findAllByItemIdIn(itemIds);
    }

    @Override
    public Booking findByBookerIdAndItemId(Long userId, Long itemId) {
        return bookingRepository.findByBookerIdAndItemId(userId, itemId);
    }

}
