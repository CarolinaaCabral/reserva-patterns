package br.upe.reservapatterns.kit.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CreateKitRequest(@NotBlank String name, String description,
                               @NotEmpty List<@Valid KitItemRequest> items) {}
