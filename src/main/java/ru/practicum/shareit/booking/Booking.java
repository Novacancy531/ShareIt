package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.model.Item;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Booking {
    private final long id;
    @NotBlank(message = "Время начала бронирования не указано.")
    private LocalDateTime start;
    @NotBlank(message = "Время окончания бронирования не указано.")
    private LocalDateTime end;
    @NotBlank(message = "Бронируемая вещь не указана.")
    private final Item item;
    @NotBlank(message = "Пользователь бронирующий вещь не указан.")
    private final long booker;
    private Status status;
}
