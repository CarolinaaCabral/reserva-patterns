package br.upe.reservapatterns.handover.entity;

import br.upe.reservapatterns.booking.entity.Booking;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "handover_plans")
public class HandoverPlan {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "booking_id", nullable = false, unique = true)
  private Booking booking;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private HandoverMode mode;

  @Column(nullable = false)
  private String pickupMethod;
  @Column(nullable = false)
  private String pickupLocation;
  private int pickupFeeCents;
  @Column(nullable = false)
  private String pickupInstructions;

  @Column(nullable = false)
  private String returnMethod;
  @Column(nullable = false)
  private String returnLocation;
  @Column(nullable = false)
  private java.time.LocalDateTime returnDueAt;
  @Column(nullable = false)
  private String returnInstructions;

  protected HandoverPlan() {}

  public HandoverPlan(Booking booking, HandoverMode mode, PickupTerms pickup,
                      ReturnTerms returns) {
    if (booking == null || mode == null || pickup == null || returns == null
            || !returns.dueAt().equals(booking.getEndsAt())) {
      throw new IllegalArgumentException("Plano de entrega inválido");
    }
    this.booking = booking;
    this.mode = mode;
    this.pickupMethod = pickup.method();
    this.pickupLocation = pickup.location();
    this.pickupFeeCents = pickup.feeCents();
    this.pickupInstructions = pickup.instructions();
    this.returnMethod = returns.method();
    this.returnLocation = returns.location();
    this.returnDueAt = returns.dueAt();
    this.returnInstructions = returns.instructions();
  }

  public Long getId() { return id; }
  public Booking getBooking() { return booking; }
  public HandoverMode getMode() { return mode; }
  public PickupTerms getPickup() {
    return new PickupTerms(pickupMethod, pickupLocation, pickupFeeCents, pickupInstructions);
  }
  public ReturnTerms getReturns() {
    return new ReturnTerms(returnMethod, returnLocation, returnDueAt, returnInstructions);
  }
}
