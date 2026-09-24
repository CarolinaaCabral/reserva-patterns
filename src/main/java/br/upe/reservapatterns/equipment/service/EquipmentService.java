package br.upe.reservapatterns.equipment.service;

import br.upe.reservapatterns.equipment.dto.CreateEquipmentRequest;
import br.upe.reservapatterns.equipment.dto.EquipmentResponse;
import br.upe.reservapatterns.equipment.entity.Equipment;
import br.upe.reservapatterns.equipment.repository.EquipmentRepository;
import br.upe.reservapatterns.exception.ConflictException;
import br.upe.reservapatterns.exception.NotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EquipmentService {
  private final EquipmentRepository repository;

  public EquipmentService(EquipmentRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public EquipmentResponse create(CreateEquipmentRequest request) {
    if (repository.existsByNameIgnoreCase(request.name().trim())) {
      throw new ConflictException("Equipamento já cadastrado");
    }
    return EquipmentResponse.from(repository.save(
            new Equipment(request.name(), request.totalUnits())));
  }

  @Transactional(readOnly = true)
  public Equipment get(Long id) {
    return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Equipamento não encontrado"));
  }

  @Transactional(readOnly = true)
  public List<EquipmentResponse> list() {
    return repository.findAll().stream().map(EquipmentResponse::from).toList();
  }
}
