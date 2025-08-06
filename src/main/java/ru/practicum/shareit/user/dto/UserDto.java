package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDto(Long id, @NotBlank(message = "Имя не может быть пустым.") String name,
                      @NotBlank(message = "Email должен быть указан.")
                      @Email(message = "Email имеет неверный формат.") String email) {
}
