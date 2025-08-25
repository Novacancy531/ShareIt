package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void testItemBuilderAndSetters() {
        User owner = new User(1L, "Aleksandr", "Dolsa.broadstaff@gmail.com");
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();

        Item item = Item.builder()
                .id(1L)
                .name("Дрель")
                .description("Супер дрель")
                .available(true)
                .owner(owner)
                .requestId(5L)
                .comments(List.of(comment1, comment2))
                .build();

        assertEquals(1L, item.getId());
        assertEquals("Дрель", item.getName());
        assertEquals("Супер дрель", item.getDescription());
        assertTrue(item.getAvailable());
        assertEquals(owner, item.getOwner());
        assertEquals(5L, item.getRequestId());
        assertEquals(2, item.getComments().size());

        item.setName("Молоток");
        assertEquals("Молоток", item.getName());
    }
}
