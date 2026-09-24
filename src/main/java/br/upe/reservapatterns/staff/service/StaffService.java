package br.upe.reservapatterns.staff.service;

import br.upe.reservapatterns.exception.ConflictException;
import br.upe.reservapatterns.exception.NotFoundException;
import br.upe.reservapatterns.staff.dto.CreateStaffRequest;
import br.upe.reservapatterns.staff.dto.StaffResponse;
import br.upe.reservapatterns.staff.entity.StaffUser;
import br.upe.reservapatterns.staff.repository.StaffRepository;
import br.upe.reservapatterns.staff.security.Role;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StaffService {
  private final StaffRepository repository;
  private final PasswordEncoder encoder;

  public StaffService(StaffRepository repository, PasswordEncoder encoder) {
    this.repository = repository;
    this.encoder = encoder;
  }

  @Transactional
  public StaffResponse create(CreateStaffRequest request) {
    String username = request.username().trim().toLowerCase(Locale.ROOT);
    if (request.role() == Role.ADMIN) {
      throw new IllegalArgumentException("ADMIN é criado apenas na inicialização");
    }
    if (repository.existsByUsername(username)) {
      throw new ConflictException("Usuário já existe");
    }
    StaffUser user = new StaffUser(username, encoder.encode(request.password()), request.role());
    return StaffResponse.from(repository.save(user));
  }

  @Transactional(readOnly = true)
  public StaffUser get(String username) {
    return repository.findByUsername(username)
            .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
  }
}
