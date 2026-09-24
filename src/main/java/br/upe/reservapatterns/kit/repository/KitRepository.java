package br.upe.reservapatterns.kit.repository;

import br.upe.reservapatterns.kit.entity.Kit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KitRepository extends JpaRepository<Kit, Long> {}

