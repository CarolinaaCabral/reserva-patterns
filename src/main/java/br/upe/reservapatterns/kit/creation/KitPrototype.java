package br.upe.reservapatterns.kit.creation;

import br.upe.reservapatterns.kit.entity.Kit;

/** Exercício 2: cópia profunda dos itens e referência aos equipamentos do catálogo. */
public class KitPrototype {
  private final Kit original;

  public KitPrototype(Kit original) {
    this.original = original;
  }

  public Kit copy() {
    throw new UnsupportedOperationException("Implementar Prototype");
  }
}
