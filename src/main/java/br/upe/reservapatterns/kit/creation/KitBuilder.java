package br.upe.reservapatterns.kit.creation;

import br.upe.reservapatterns.equipment.entity.Equipment;
import br.upe.reservapatterns.kit.entity.Kit;
import java.util.ArrayList;
import java.util.List;

/** Exercício 1: monte um novo kit e itens independentes em cada build(). */
public class KitBuilder {
  private final String name;
  private String description = "";
  private final List<Entry> entries = new ArrayList<>();

  public KitBuilder(String name) {
    this.name = name;
  }

  public KitBuilder description(String description) {
    throw new UnsupportedOperationException("Implementar Builder");
  }

  public KitBuilder add(Equipment equipment, int quantity) {
    throw new UnsupportedOperationException("Implementar Builder");
  }

  public Kit build() {
    throw new UnsupportedOperationException("Implementar Builder");
  }

  private record Entry(Equipment equipment, int quantity) {}
}
