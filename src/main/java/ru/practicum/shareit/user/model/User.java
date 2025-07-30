package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class User {
    private final Long id;
    private String name;
    private String email;
}
