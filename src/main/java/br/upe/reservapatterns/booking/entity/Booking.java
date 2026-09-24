package br.upe.reservapatterns.booking.entity;

import br.upe.reservapatterns.equipment.entity.Equipment;
import br.upe.reservapatterns.exception.ConflictException;
import br.upe.reservapatterns.kit.entity.Kit;
import br.upe.reservapatterns.staff.entity.StaffUser;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "kit_id", nullable = false)
  private Kit kit;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "requester_id", nullable = false)
  private StaffUser requester;

  private LocalDateTime startsAt;
  private LocalDateTime endsAt;

  @Enumerated(EnumType.STRING)
  private BookingKind kind;

  @Enumerated(EnumType.STRING)
  private BookingStatus status;

  @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderColumn(name = "line_order")
  private List<BookingItem> items = new ArrayList<>();

  protected Booking() {}

  public Booking(Kit kit, StaffUser requester, LocalDateTime startsAt, LocalDateTime endsAt,
                 BookingKind kind, BookingStatus status) {
    if (kit == null || requester == null || startsAt == null || endsAt == null
            || !endsAt.isAfter(startsAt) || kind == null || status == null) {
      throw new IllegalArgumentException("Reserva inválida");
    }
    this.kit = kit;
    this.requester = requester;
    this.startsAt = startsAt;
    this.endsAt = endsAt;
    this.kind = kind;
    this.status = status;
  }

  public void addItem(Equipment equipment, int quantity) {
    items.add(new BookingItem(this, equipment, quantity));
  }

  public void approve() {
    if (status != BookingStatus.PENDING) {
      throw new ConflictException("Somente reservas pendentes podem ser aprovadas");
    }
    status = BookingStatus.CONFIRMED;
  }

  public Long getId() { return id; }
  public Kit getKit() { return kit; }
  public StaffUser getRequester() { return requester; }
  public LocalDateTime getStartsAt() { return startsAt; }
  public LocalDateTime getEndsAt() { return endsAt; }
  public BookingKind getKind() { return kind; }
  public BookingStatus getStatus() { return status; }
  public List<BookingItem> getItems() { return Collections.unmodifiableList(items); }
}
