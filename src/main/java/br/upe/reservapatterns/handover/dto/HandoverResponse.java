package br.upe.reservapatterns.handover.dto;

import br.upe.reservapatterns.handover.entity.HandoverMode;
import br.upe.reservapatterns.handover.entity.HandoverPlan;
import br.upe.reservapatterns.handover.entity.PickupTerms;
import br.upe.reservapatterns.handover.entity.ReturnTerms;

public record HandoverResponse(Long id, Long bookingId, HandoverMode mode,
                               PickupTerms pickup, ReturnTerms returns) {
  public static HandoverResponse from(HandoverPlan plan) {
    return new HandoverResponse(plan.getId(), plan.getBooking().getId(), plan.getMode(),
            plan.getPickup(), plan.getReturns());
  }
}
