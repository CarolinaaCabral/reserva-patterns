package br.upe.reservapatterns.staff.controller;

import br.upe.reservapatterns.staff.dto.CreateStaffRequest;
import br.upe.reservapatterns.staff.dto.StaffResponse;
import br.upe.reservapatterns.staff.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/staff/users")
public class StaffController {
  private final StaffService service;

  public StaffController(StaffService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public StaffResponse create(@Valid @RequestBody CreateStaffRequest request) {
    return service.create(request);
  }
}
