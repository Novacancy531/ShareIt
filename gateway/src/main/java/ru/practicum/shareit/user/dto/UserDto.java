package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public final class UserDto {
    Long id;

    @NotBlank(message = "Имя не может быть пустым.")
    @Size(max = 50)
    String name;

    @NotBlank(message = "Email должен быть указан.")
    @Email(message = "Email имеет неверный формат.")
    String email;
}
