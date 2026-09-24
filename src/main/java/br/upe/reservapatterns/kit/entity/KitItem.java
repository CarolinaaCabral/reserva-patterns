package br.upe.reservapatterns.kit.entity;

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
@Table(name = "kit_items")
public class KitItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "kit_id", nullable = false)
  private Kit kit;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "equipment_id", nullable = false)
  private Equipment equipment;

  private int quantity;

  protected KitItem() {}

  KitItem(Kit kit, Equipment equipment, int quantity) {
    if (kit == null || equipment == null || quantity <= 0
            || quantity > equipment.getTotalUnits()) {
      throw new IllegalArgumentException("Item inválido");
    }
    this.kit = kit;
    this.equipment = equipment;
    this.quantity = quantity;
  }

  public void changeQuantity(int newQuantity) {
    if (newQuantity <= 0 || newQuantity > equipment.getTotalUnits()) {
      throw new IllegalArgumentException("Quantidade fora do estoque");
    }
    quantity = newQuantity;
  }

  public Long getId() { return id; }
  public Equipment getEquipment() { return equipment; }
  public int getQuantity() { return quantity; }
}
