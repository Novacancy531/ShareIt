package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ItemDto {
    Long id;
    @NotBlank(message = "Имя должно быть заполнено.")
    String name;
    @NotBlank(message = "Описание должно быть заполнено.")
    String description;
    @NotNull
    Boolean available;
    Long owner;
    Long itemRequest;
}
