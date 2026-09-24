package br.upe.reservapatterns.kit.entity;

import br.upe.reservapatterns.equipment.entity.Equipment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "kits")
public class Kit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @OneToMany(mappedBy = "kit", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderColumn(name = "line_order")
  private List<KitItem> items = new ArrayList<>();

  protected Kit() {}

  public Kit(String name, String description) {
    if (name == null || name.isBlank() || description == null) {
      throw new IllegalArgumentException("Kit inválido");
    }
    this.name = name.trim();
    this.description = description;
  }

  public void add(Equipment equipment, int quantity) {
    items.add(new KitItem(this, equipment, quantity));
  }

  public void rename(String newName) {
    if (newName == null || newName.isBlank()) {
      throw new IllegalArgumentException("Nome inválido");
    }
    name = newName.trim();
  }

  public Long getId() { return id; }
  public String getName() { return name; }
  public String getDescription() { return description; }
  public List<KitItem> getItems() { return Collections.unmodifiableList(items); }
}
