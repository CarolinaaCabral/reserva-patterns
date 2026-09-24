package br.upe.reservapatterns.handover.creation;

import br.upe.reservapatterns.booking.entity.Booking;
import br.upe.reservapatterns.handover.entity.PickupTerms;
import br.upe.reservapatterns.handover.entity.ReturnTerms;

/** Exercício 4: produtos da mesma família para retirada e devolução. */
public interface HandoverFactory {
  PickupTerms pickup(Booking booking, String destination);

  ReturnTerms returns(Booking booking, String destination);
}
