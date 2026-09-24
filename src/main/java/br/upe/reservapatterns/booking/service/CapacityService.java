package br.upe.reservapatterns.booking.service;

import br.upe.reservapatterns.booking.entity.Booking;
import br.upe.reservapatterns.booking.entity.BookingItem;
import br.upe.reservapatterns.booking.entity.BookingStatus;
import br.upe.reservapatterns.booking.repository.BookingRepository;
import br.upe.reservapatterns.equipment.repository.EquipmentRepository;
import br.upe.reservapatterns.exception.ConflictException;
import br.upe.reservapatterns.exception.NotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CapacityService {
  private final EquipmentRepository equipment;
  private final BookingRepository bookings;

  public CapacityService(EquipmentRepository equipment, BookingRepository bookings) {
    this.equipment = equipment;
    this.bookings = bookings;
  }

  /** Chamado dentro da transação de criação; trava os equipamentos em ordem. */
  public void ensureAvaipatternsle(Booking candidate) {
    List<Long> ids = candidate.getItems().stream().map(item -> item.getEquipment().getId())
            .distinct().sorted().toList();
    Map<Long, Integer> stocks = new HashMap<>();
    for (Long id : ids) {
      int total = equipment.lockById(id)
              .orElseThrow(() -> new NotFoundException("Equipamento não encontrado"))
              .getTotalUnits();
      stocks.put(id, total);
    }
    List<Booking> overlapping = bookings.overlapping(candidate.getStartsAt(),
            candidate.getEndsAt(), BookingStatus.CANCELLED);
    for (Long id : ids) {
      int inUse = overlapping.stream()
              .flatMap(booking -> booking.getItems().stream())
              .filter(item -> item.getEquipment().getId().equals(id))
              .mapToInt(BookingItem::getQuantity).sum();
      int requested = candidate.getItems().stream()
              .filter(item -> item.getEquipment().getId().equals(id))
              .mapToInt(BookingItem::getQuantity).sum();
      if (inUse + requested > stocks.get(id)) {
        throw new ConflictException("Equipamento indisponível no período");
      }
    }
  }
}
