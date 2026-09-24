package br.upe.reservapatterns.booking.dto;

import br.upe.reservapatterns.booking.entity.BookingItem;

public record BookingItemResponse(Long equipmentId, String equipmentName, int quantity) {
  public static BookingItemResponse from(BookingItem item) {
    return new BookingItemResponse(item.getEquipment().getId(),
            item.getEquipment().getName(), item.getQuantity());
  }
}
