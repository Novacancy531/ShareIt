package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;
import java.util.List;

public interface BookingService {

    BookingDto createBooking(long userId, BookingCreateDto bookingCreateDto);

    BookingDto approvedBooking(long userId, long bookingId, boolean approved);

    BookingDto getBooking(long userId, long bookingId);

    List<BookingDto> getBookings(Long userId, String state);

    List<BookingDto> getBookingsForOwner(Long userId, String state);

    List<Booking> findAllByItemIdIn(Collection<Long> itemIds);

    Booking findByBookerIdAndItemId(Long userId, Long itemId);
}
