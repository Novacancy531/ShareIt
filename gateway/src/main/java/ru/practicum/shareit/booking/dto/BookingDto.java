package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Builder
@Value
public class BookingDto {
    Long id;

    @NotNull(message = "Время начала бронирования не указано.")
    LocalDateTime start;

    @Future
    @NotNull(message = "Время окончания бронирования не указано.")
    LocalDateTime end;

    @NotNull(message = "Бронируемая вещь не указана.")
    ItemDto item;

    UserDto booker;

    Status status;
}
