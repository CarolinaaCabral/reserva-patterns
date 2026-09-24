package br.upe.reservapatterns.handover.repository;

import br.upe.reservapatterns.handover.entity.HandoverPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HandoverRepository extends JpaRepository<HandoverPlan, Long> {
  boolean existsByBookingId(Long bookingId);
}
