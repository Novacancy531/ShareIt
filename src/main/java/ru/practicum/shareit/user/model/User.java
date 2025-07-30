package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@Builder
@Data
public class User {
    @With
    private final Long id;
    private String name;
    private String email;
}
