package br.upe.reservapatterns.booking.repository;

import java.time.LocalDateTime;
import java.util.List;

import br.upe.reservapatterns.booking.entity.Booking;
import br.upe.reservapatterns.booking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {
  @Query("select distinct b from Booking b left join fetch b.items "
          + "where b.status <> :cancelled "
          + "and b.startsAt < :end and b.endsAt > :start")
  List<Booking> overlapping(@Param("start") LocalDateTime start,
                            @Param("end") LocalDateTime end,
                            @Param("cancelled") BookingStatus cancelled);
}
