package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    @Test
    void testRequestBuilderAndSetters() {
        User user = new User(1L, "Aleksandr", "Dolsa.broadstaff@gmail.com");
        Timestamp now = Timestamp.from(Instant.now());

        Request request = Request.builder()
                .id(1L)
                .description("Описание запроса")
                .requestor(user)
                .created(now)
                .build();

        assertEquals(1L, request.getId());
        assertEquals("Описание запроса", request.getDescription());
        assertEquals(user, request.getRequestor());
        assertEquals(now, request.getCreated());

        request.setDescription("Обновленное описание");
        assertEquals("Обновленное описание", request.getDescription());
    }
}
