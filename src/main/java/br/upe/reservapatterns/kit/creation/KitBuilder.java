package br.upe.reservapatterns.kit.creation;

import br.upe.reservapatterns.equipment.entity.Equipment;
import br.upe.reservapatterns.kit.entity.Kit;
import br.upe.reservapatterns.kit.entity.KitItem;

import java.util.ArrayList;
import java.util.List;

/** Exercício 1: monte um novo kit e itens independentes em cada build(). */
public class KitBuilder {
  private final String name;
  private String description = "";
  private final List<Entry> entries = new ArrayList<>();

  public KitBuilder(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Nome não pode ser vazio");
    }
    this.name = name;
  }

  public KitBuilder description(String description) {
    this.description = description;
    return this;
  }

  public KitBuilder add(Equipment equipment, int quantity) {
    if (quantity <= 0 || quantity > equipment.getTotalUnits()) {
        throw new IllegalArgumentException("Quantidade invalida");
    }

    for (Entry entry : entries) {
        if (entry.equipment.equals(equipment)) {
            throw new IllegalArgumentException("Equipamento já adicionado");
        }
    }

    this.entries.add(new Entry(equipment, quantity));
    return this;
  }

  public Kit build() {
    if (this.entries.isEmpty()) {
        throw new IllegalArgumentException("Nenhum equipamento encontrado");
    }

    Kit kit = new Kit(this.name, this.description);

    for  (Entry entry : this.entries) {
        kit.add(entry.equipment, entry.quantity);
    }

    return kit;
  }

  private record Entry(Equipment equipment, int quantity) {}
}
