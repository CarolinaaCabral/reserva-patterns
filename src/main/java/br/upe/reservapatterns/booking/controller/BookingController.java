package br.upe.reservapatterns.booking.controller;

import br.upe.reservapatterns.booking.dto.BookingResponse;
import br.upe.reservapatterns.booking.dto.CreateBookingRequest;
import br.upe.reservapatterns.booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
  private final BookingService service;

  public BookingController(BookingService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BookingResponse create(@Valid @RequestBody CreateBookingRequest request,
                                @AuthenticationPrincipal Jwt jwt) {
    return service.create(request, jwt.getSubject());
  }

  @GetMapping("/{id}")
  public BookingResponse find(@PathVariable long id, @AuthenticationPrincipal Jwt jwt) {
    return service.find(id, jwt.getSubject());
  }

  @PatchMapping("/{id}/approve")
  public BookingResponse approve(@PathVariable long id) {
    return service.approve(id);
  }
}
