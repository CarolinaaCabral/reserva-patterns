package br.upe.reservapatterns.equipment.controller;

import br.upe.reservapatterns.equipment.dto.CreateEquipmentRequest;
import br.upe.reservapatterns.equipment.dto.EquipmentResponse;
import br.upe.reservapatterns.equipment.service.EquipmentService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/equipments")
public class EquipmentController {
  private final EquipmentService service;

  public EquipmentController(EquipmentService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public EquipmentResponse create(@Valid @RequestBody CreateEquipmentRequest request) {
    return service.create(request);
  }

  @GetMapping
  public List<EquipmentResponse> list() {
    return service.list();
  }

  @GetMapping("/{id}")
  public EquipmentResponse get(@PathVariable Long id) {
    return EquipmentResponse.from(service.get(id));
  }
}
