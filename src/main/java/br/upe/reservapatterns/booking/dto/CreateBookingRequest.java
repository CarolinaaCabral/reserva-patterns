package br.upe.reservapatterns.booking.dto;

import br.upe.reservapatterns.booking.entity.BookingKind;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateBookingRequest(@NotNull Long kitId,
                                   @NotNull LocalDateTime startsAt,
                                   @NotNull BookingKind kind) {}
