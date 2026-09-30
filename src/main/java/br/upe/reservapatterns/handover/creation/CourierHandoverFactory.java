package br.upe.reservapatterns.handover.creation;

import br.upe.reservapatterns.booking.entity.Booking;
import br.upe.reservapatterns.handover.entity.PickupTerms;
import br.upe.reservapatterns.handover.entity.ReturnTerms;
import org.springframework.stereotype.Component;

@Component("COURIER")
public class CourierHandoverFactory implements HandoverFactory {
  @Override
  public PickupTerms pickup(Booking booking, String destination) {
    PickupTerms terms = new PickupTerms(
            "COURIER",
            destination.strip(),
            1500,
            "Entregando por mensageiro"
    );

    return terms;
  }

  @Override
  public ReturnTerms returns(Booking booking, String destination) {
    ReturnTerms terms = new ReturnTerms(
            "COURIER",
            destination.strip(),
            booking.getEndsAt(),
            "Devolução por mensageiro"
    );

    return terms;
  }
}
