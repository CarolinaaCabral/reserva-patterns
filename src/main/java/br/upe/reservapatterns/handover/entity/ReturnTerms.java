package br.upe.reservapatterns.handover.entity;

import java.time.LocalDateTime;

public record ReturnTerms(String method, String location, LocalDateTime dueAt,
                          String instructions) {
  public ReturnTerms {
    if (method == null || method.isBlank() || location == null || location.isBlank()
            || dueAt == null || instructions == null || instructions.isBlank()) {
      throw new IllegalArgumentException("Devolução inválida");
    }
  }
}
