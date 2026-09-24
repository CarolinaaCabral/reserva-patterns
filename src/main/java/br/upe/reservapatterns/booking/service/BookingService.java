package br.upe.reservapatterns.booking.service;

import br.upe.reservapatterns.booking.creation.BookingCreator;
import br.upe.reservapatterns.booking.dto.BookingResponse;
import br.upe.reservapatterns.booking.dto.CreateBookingRequest;
import br.upe.reservapatterns.booking.entity.Booking;
import br.upe.reservapatterns.booking.repository.BookingRepository;
import br.upe.reservapatterns.exception.NotFoundException;
import br.upe.reservapatterns.kit.entity.Kit;
import br.upe.reservapatterns.kit.service.KitService;
import br.upe.reservapatterns.staff.entity.StaffUser;
import br.upe.reservapatterns.staff.security.Role;
import br.upe.reservapatterns.staff.service.StaffService;
import java.util.Map;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {
  private final BookingRepository bookings;
  private final KitService kits;
  private final StaffService staff;
  private final CapacityService capacity;
  private final Map<String, BookingCreator> creators;

  public BookingService(BookingRepository bookings, KitService kits, StaffService staff,
                        CapacityService capacity, Map<String, BookingCreator> creators) {
    this.bookings = bookings;
    this.kits = kits;
    this.staff = staff;
    this.capacity = capacity;
    this.creators = creators;
  }

  @Transactional
  public BookingResponse create(CreateBookingRequest request, String username) {
    Kit kit = kits.get(request.kitId());
    StaffUser requester = staff.get(username);
    BookingCreator creator = creators.get(request.kind().name());
    if (creator == null) {
      throw new IllegalArgumentException("Tipo de reserva inválido");
    }
    Booking booking = creator.open(kit, requester, request.startsAt());
    capacity.ensureAvaipatternsle(booking);
    return BookingResponse.from(bookings.saveAndFlush(booking));
  }

  @Transactional(readOnly = true)
  public Booking get(long id) {
    return bookings.findById(id)
            .orElseThrow(() -> new NotFoundException("Reserva não encontrada"));
  }

  @Transactional(readOnly = true)
  public BookingResponse find(long id, String username) {
    return BookingResponse.from(visible(id, username));
  }

  @Transactional(readOnly = true)
  public Booking visible(long id, String username) {
    Booking booking = get(id);
    StaffUser viewer = staff.get(username);
    boolean privileged = viewer.getRole() == Role.ADMIN || viewer.getRole() == Role.STAFF;
    if (!privileged && !booking.getRequester().getId().equals(viewer.getId())) {
      throw new AccessDeniedException("Reserva de outro usuário");
    }
    return booking;
  }

  @Transactional
  public BookingResponse approve(long id) {
    Booking booking = get(id);
    booking.approve();
    return BookingResponse.from(booking);
  }
}
