package br.upe.reservapatterns.equipment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "equipments")
public class Equipment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 120)
  private String name;

  @Column(nullable = false)
  private int totalUnits;

  protected Equipment() {}

  public Equipment(String name, int totalUnits) {
    if (name == null || name.isBlank() || totalUnits < 1) {
      throw new IllegalArgumentException("Equipamento inválido");
    }
    this.name = name.trim();
    this.totalUnits = totalUnits;
  }

  public Long getId() { return id; }
  public String getName() { return name; }
  public int getTotalUnits() { return totalUnits; }
}
