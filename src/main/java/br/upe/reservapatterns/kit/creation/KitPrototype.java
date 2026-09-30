package br.upe.reservapatterns.kit.creation;

import br.upe.reservapatterns.equipment.entity.Equipment;
import br.upe.reservapatterns.kit.entity.Kit;
import br.upe.reservapatterns.kit.entity.KitItem;

/** Exercício 2: cópia profunda dos itens e referência aos equipamentos do catálogo. */
public class KitPrototype {
  private final Kit original;

  public KitPrototype(Kit original) {
    if (original == null) {
        throw new IllegalArgumentException("O original nao pode ser nulo.");
    }
      this.original = original;
  }

  public Kit copy() {
    Kit kit = new Kit (original.getName(),original.getDescription());
    for (KitItem kitItem : original.getItems()) {
        Equipment equipamento = kitItem.getEquipment();
        Integer quantity = kitItem.getQuantity();
        kit.add(equipamento, quantity);
    }
    return kit;
  }
}
