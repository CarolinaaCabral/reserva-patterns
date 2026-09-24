package br.upe.reservapatterns.handover.service;

import java.util.Map;

import br.upe.reservapatterns.booking.entity.Booking;
import br.upe.reservapatterns.booking.entity.BookingStatus;
import br.upe.reservapatterns.booking.service.BookingService;
import br.upe.reservapatterns.exception.ConflictException;
import br.upe.reservapatterns.exception.NotFoundException;
import br.upe.reservapatterns.handover.creation.HandoverFactory;
import br.upe.reservapatterns.handover.dto.CreateHandoverRequest;
import br.upe.reservapatterns.handover.dto.HandoverResponse;
import br.upe.reservapatterns.handover.entity.HandoverPlan;
import br.upe.reservapatterns.handover.entity.PickupTerms;
import br.upe.reservapatterns.handover.entity.ReturnTerms;
import br.upe.reservapatterns.handover.repository.HandoverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HandoverService {
  private final HandoverRepository plans;
  private final BookingService bookings;
  private final Map<String, HandoverFactory> factories;

  public HandoverService(HandoverRepository plans, BookingService bookings,
                         Map<String, HandoverFactory> factories) {
    this.plans = plans;
    this.bookings = bookings;
    this.factories = factories;
  }

  @Transactional
  public HandoverResponse create(CreateHandoverRequest request) {
    Booking booking = bookings.get(request.bookingId());
    if (booking.getStatus() != BookingStatus.CONFIRMED) {
      throw new ConflictException("A reserva ainda não foi confirmada");
    }
    if (plans.existsByBookingId(booking.getId())) {
      throw new ConflictException("A reserva já tem plano de entrega");
    }
    HandoverFactory factory = factories.get(request.mode().name());
    if (factory == null) {
      throw new IllegalArgumentException("Modalidade de entrega inválida");
    }
    PickupTerms pickup = factory.pickup(booking, request.destination());
    ReturnTerms returns = factory.returns(booking, request.destination());
    return HandoverResponse.from(plans.saveAndFlush(new HandoverPlan(booking, request.mode(),
            pickup, returns)));
  }

  @Transactional(readOnly = true)
  public HandoverResponse find(long id, String username) {
    HandoverPlan plan = plans.findById(id)
            .orElseThrow(() -> new NotFoundException("Plano de entrega não encontrado"));
    bookings.visible(plan.getBooking().getId(), username);
    return HandoverResponse.from(plan);
  }
}
