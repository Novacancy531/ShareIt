package ru.practicum.shareit.booking.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Builder
@Value
public class BookingDto {
    Long id;

    @FutureOrPresent
    @NotNull(message = "Время начала бронирования не указано.")
    LocalDateTime start;

    @Future
    @NotNull(message = "Время окончания бронирования не указано.")
    LocalDateTime end;

    @NotNull(message = "Бронируемая вещь не указана.")
    Long itemId;

    Long bookerId;

    @Enumerated(EnumType.STRING)
    Status status;
}
