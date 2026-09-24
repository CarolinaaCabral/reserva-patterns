package br.upe.reservapatterns.staff.controller;

import br.upe.reservapatterns.staff.dto.LoginRequest;
import br.upe.reservapatterns.staff.dto.LoginResponse;
import br.upe.reservapatterns.staff.security.TokenService;
import br.upe.reservapatterns.staff.service.StaffService;
import jakarta.validation.Valid;
import java.util.Locale;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final StaffService staff;
  private final TokenService tokens;

  public AuthController(AuthenticationManager authenticationManager, StaffService staff,
                        TokenService tokens) {
    this.authenticationManager = authenticationManager;
    this.staff = staff;
    this.tokens = tokens;
  }

  @PostMapping("/login")
  public LoginResponse login(@Valid @RequestBody LoginRequest request) {
    String username = request.username().trim().toLowerCase(Locale.ROOT);
    authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(username, request.password()));
    return new LoginResponse(tokens.issue(staff.get(username)), "Bearer");
  }
}
