package br.upe.reservapatterns.kit.dto;

import jakarta.validation.constraints.Positive;

public record ChangeQuantityRequest(@Positive int quantity) {}
