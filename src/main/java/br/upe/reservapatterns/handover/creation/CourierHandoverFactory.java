package br.upe.reservapatterns.handover.creation;

import br.upe.reservapatterns.booking.entity.Booking;
import br.upe.reservapatterns.handover.entity.PickupTerms;
import br.upe.reservapatterns.handover.entity.ReturnTerms;
import org.springframework.stereotype.Component;

@Component("COURIER")
public class CourierHandoverFactory implements HandoverFactory {
  @Override
  public PickupTerms pickup(Booking booking, String destination) {
    throw new UnsupportedOperationException("Implementar envio por mensageiro");
  }

  @Override
  public ReturnTerms returns(Booking booking, String destination) {
    throw new UnsupportedOperationException("Implementar recolhimento por mensageiro");
  }
}
