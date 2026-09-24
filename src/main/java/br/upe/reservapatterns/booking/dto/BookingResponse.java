package br.upe.reservapatterns.booking.dto;

import br.upe.reservapatterns.booking.entity.Booking;
import br.upe.reservapatterns.booking.entity.BookingKind;
import br.upe.reservapatterns.booking.entity.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public record BookingResponse(Long id, Long kitId, String requester, LocalDateTime startsAt,
                              LocalDateTime endsAt, BookingKind kind, BookingStatus status,
                              List<BookingItemResponse> items) {
  public static BookingResponse from(Booking booking) {
    return new BookingResponse(booking.getId(), booking.getKit().getId(),
            booking.getRequester().getUsername(), booking.getStartsAt(), booking.getEndsAt(),
            booking.getKind(), booking.getStatus(),
            booking.getItems().stream().map(BookingItemResponse::from).toList());
  }
}

