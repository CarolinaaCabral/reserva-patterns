package br.upe.reservapatterns.handover.controller;

import br.upe.reservapatterns.handover.dto.CreateHandoverRequest;
import br.upe.reservapatterns.handover.dto.HandoverResponse;
import br.upe.reservapatterns.handover.service.HandoverService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/handovers")
public class HandoverController {
  private final HandoverService service;

  public HandoverController(HandoverService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public HandoverResponse create(@Valid @RequestBody CreateHandoverRequest request) {
    return service.create(request);
  }

  @GetMapping("/{id}")
  public HandoverResponse find(@PathVariable long id, @AuthenticationPrincipal Jwt jwt) {
    return service.find(id, jwt.getSubject());
  }
}
