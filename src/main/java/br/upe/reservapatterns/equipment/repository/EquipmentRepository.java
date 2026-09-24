package br.upe.reservapatterns.equipment.repository;

import br.upe.reservapatterns.equipment.entity.Equipment;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
  boolean existsByNameIgnoreCase(String name);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select e from Equipment e where e.id = :id")
  Optional<Equipment> lockById(@Param("id") Long id);
}
