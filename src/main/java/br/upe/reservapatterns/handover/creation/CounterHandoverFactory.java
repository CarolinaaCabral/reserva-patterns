package br.upe.reservapatterns.handover.creation;

import br.upe.reservapatterns.booking.entity.Booking;
import br.upe.reservapatterns.handover.entity.PickupTerms;
import br.upe.reservapatterns.handover.entity.ReturnTerms;
import org.springframework.stereotype.Component;

@Component("COUNTER")
public class CounterHandoverFactory implements HandoverFactory {
  @Override
  public PickupTerms pickup(Booking booking, String destination) {
    PickupTerms terms = new PickupTerms(
            "COUNTER",
            "Laboratório Central",
            0,
            "Retire no balcão"
    );

    return terms;
  }

  @Override
  public ReturnTerms returns(Booking booking, String destination) {
    ReturnTerms terms = new ReturnTerms(
            "COUNTER",
            "Laboratório Central",
            booking.getEndsAt(),
            "Devolva no balcão"
    );

    return terms;
  }
}

