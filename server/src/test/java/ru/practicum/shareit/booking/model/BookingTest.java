package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    void testBooking() {
        User user = new User(1L, "Aleksandr", "Dolsa.broadstaff@gmail.com");
        Item item = Item.builder()
                .id(1L)
                .name("Дрель")
                .description("Супер дрель")
                .available(true)
                .owner(null)
                .requestId(null)
                .build();

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);

        Booking booking = Booking.builder()
                .id(1L)
                .booker(user)
                .item(item)
                .start(start)
                .end(end)
                .status(Status.APPROVED)
                .build();

        assertEquals(1L, booking.getId());
        assertEquals(user, booking.getBooker());
        assertEquals(item, booking.getItem());
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(Status.APPROVED, booking.getStatus());

        booking.setStatus(Status.WAITING);
        assertEquals(Status.WAITING, booking.getStatus());
    }
}
