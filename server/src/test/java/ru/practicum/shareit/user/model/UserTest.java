package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserBuilderAndSetters() {
        User user = User.builder()
                .id(1L)
                .name("Aleksandr")
                .email("Dolsa.broadstaff@gmail.com")
                .build();

        assertEquals(1L, user.getId());
        assertEquals("Aleksandr", user.getName());
        assertEquals("Dolsa.broadstaff@gmail.com", user.getEmail());

        user.setName("Александр");
        user.setEmail("Novacancy531@yandex.ru");

        assertEquals("Александр", user.getName());
        assertEquals("Novacancy531@yandex.ru", user.getEmail());
    }
}
