package br.upe.reservapatterns.equipment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateEquipmentRequest(@NotBlank @Size(max = 120) String name,
                                     @NotNull @Min(1) Integer totalUnits) {}
