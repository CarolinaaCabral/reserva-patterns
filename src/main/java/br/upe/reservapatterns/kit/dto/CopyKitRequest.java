package br.upe.reservapatterns.kit.dto;

import jakarta.validation.constraints.NotBlank;

public record CopyKitRequest(@NotBlank String name) {}
