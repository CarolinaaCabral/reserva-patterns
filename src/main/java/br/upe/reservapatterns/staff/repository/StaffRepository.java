package br.upe.reservapatterns.staff.repository;

import br.upe.reservapatterns.staff.entity.StaffUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<StaffUser, Long> {
  Optional<StaffUser> findByUsername(String username);

  boolean existsByUsername(String username);
}
