package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class Item {
    private final Long id;
    private String name;
    private String description;
    private Boolean available;
    private final Long owner;
    private Long itemRequest;
}
