package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public final class UserDto {
    Long id;
    String name;
    String email;
}
