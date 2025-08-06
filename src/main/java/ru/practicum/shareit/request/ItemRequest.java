package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ItemRequest {
    private final long id;
    @NotBlank(message = "Описание должно быть заполнено.")
    private String description;
    @NotBlank(message = "Запрашивающий пользователь должен быть указан.")
    private final long requestor;
    private final Timestamp created;
}
