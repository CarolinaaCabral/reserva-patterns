package br.upe.reservapatterns.handover.entity;

public record PickupTerms(String method, String location, int feeCents,
                          String instructions) {
  public PickupTerms {
    if (method == null || method.isBlank() || location == null || location.isBlank()
            || feeCents < 0 || instructions == null || instructions.isBlank()) {
      throw new IllegalArgumentException("Retirada inválida");
    }
  }
}
