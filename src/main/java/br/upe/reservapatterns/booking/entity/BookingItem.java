package br.upe.reservapatterns.booking.entity;

import br.upe.reservapatterns.equipment.entity.Equipment;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "booking_items")
public class BookingItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "booking_id", nullable = false)
  private Booking booking;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "equipment_id", nullable = false)
  private Equipment equipment;

  private int quantity;

  protected BookingItem() {}

  BookingItem(Booking booking, Equipment equipment, int quantity) {
    if (booking == null || equipment == null || quantity <= 0) {
      throw new IllegalArgumentException("Item da reserva inválido");
    }
    this.booking = booking;
    this.equipment = equipment;
    this.quantity = quantity;
  }

  public Long getId() { return id; }
  public Equipment getEquipment() { return equipment; }
  public int getQuantity() { return quantity; }
}
