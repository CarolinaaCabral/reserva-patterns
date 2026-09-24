package br.upe.reservapatterns.booking.creation;

import java.time.LocalDateTime;

import br.upe.reservapatterns.booking.entity.Booking;
import br.upe.reservapatterns.kit.entity.Kit;
import br.upe.reservapatterns.staff.entity.StaffUser;
import org.springframework.stereotype.Component;

@Component("LECTURE")
public class LectureBookingCreator extends BookingCreator {
  @Override
  protected Booking create(Kit kit, StaffUser requester, LocalDateTime startsAt) {
    throw new UnsupportedOperationException("Implementar reserva para aula");
  }
}
