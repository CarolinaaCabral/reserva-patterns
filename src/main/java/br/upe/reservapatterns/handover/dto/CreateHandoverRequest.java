package br.upe.reservapatterns.handover.dto;

import br.upe.reservapatterns.handover.entity.HandoverMode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateHandoverRequest(@NotNull Long bookingId, @NotNull HandoverMode mode,
                                    @Size(max = 200) String destination) {}
