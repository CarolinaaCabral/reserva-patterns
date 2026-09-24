package br.upe.reservapatterns.staff.dto;

import br.upe.reservapatterns.staff.security.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateStaffRequest(@NotBlank @Size(max = 80) String username,
                                 @NotBlank @Size(min = 8) String password,
                                 @NotNull Role role) {}
