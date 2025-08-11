package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingService {

    BookingDto createBooking(long userId, BookingCreateDto bookingCreateDto);

    BookingDto approvedBooking(long userId, long booking_id, boolean approved);

    BookingDto getBooking(long userId, long bookingId);
}
