package br.upe.reservapatterns.booking.creation;

import br.upe.reservapatterns.booking.entity.Booking;
import br.upe.reservapatterns.kit.entity.Kit;
import br.upe.reservapatterns.kit.entity.KitItem;
import br.upe.reservapatterns.staff.entity.StaffUser;
import java.time.LocalDateTime;

/** Exercício 3: fluxo comum e método de fábrica sobrescrito. */
public abstract class BookingCreator {
  public final Booking open(Kit kit, StaffUser requester, LocalDateTime startsAt) {
      if (kit == null || kit.getItems().isEmpty()) {
          throw new IllegalArgumentException("Kit nao pode ser nulo ou vazio.");
      }

    Booking booking = create(kit, requester, startsAt);
    for (KitItem kitItem : kit.getItems()) {
        booking.addItem(kitItem.getEquipment(), kitItem.getQuantity());
    }
    return booking;
  }

  protected abstract Booking create(Kit kit, StaffUser requester, LocalDateTime startsAt);
}

