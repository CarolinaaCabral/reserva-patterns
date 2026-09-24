package br.upe.reservapatterns.kit.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record KitItemRequest(@NotNull Long equipmentId, @Positive int quantity) {}

