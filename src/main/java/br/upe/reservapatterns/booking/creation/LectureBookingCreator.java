package br.upe.reservapatterns.booking.creation;

import java.time.LocalDateTime;

import br.upe.reservapatterns.booking.entity.Booking;
import br.upe.reservapatterns.booking.entity.BookingKind;
import br.upe.reservapatterns.booking.entity.BookingStatus;
import br.upe.reservapatterns.kit.entity.Kit;
import br.upe.reservapatterns.staff.entity.StaffUser;
import org.springframework.stereotype.Component;

@Component("LECTURE")
public class LectureBookingCreator extends BookingCreator {
  @Override
  protected Booking create(Kit kit, StaffUser requester, LocalDateTime startsAt) {
      LocalDateTime endsAt = startsAt.plusHours(4);
      Booking book = new Booking(kit, requester, startsAt, endsAt, BookingKind.LECTURE, BookingStatus.CONFIRMED);
      return book;
  }
}
