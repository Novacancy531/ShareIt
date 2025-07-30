package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@Builder(toBuilder = true)
@Data
public class User {
    private final Long id;
    private String name;
    private String email;
}
