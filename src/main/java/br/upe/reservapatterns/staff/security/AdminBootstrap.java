package br.upe.reservapatterns.staff.security;

import br.upe.reservapatterns.staff.entity.StaffUser;
import br.upe.reservapatterns.staff.repository.StaffRepository;
import br.upe.reservapatterns.staff.security.Role;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class AdminBootstrap implements ApplicationRunner {
  private final StaffRepository users;
  private final PasswordEncoder passwords;
  private final String username;
  private final String password;

  public AdminBootstrap(StaffRepository users, PasswordEncoder passwords,
                        @Value("${reserva.bootstrap.username}") String username,
                        @Value("${reserva.bootstrap.password}") String password) {
    this.users = users;
    this.passwords = passwords;
    this.username = username.trim().toLowerCase(Locale.ROOT);
    this.password = password;
  }

  @Override
  public void run(ApplicationArguments args) {
    if (username.isBlank() || password.length() < 8) {
      throw new IllegalArgumentException("Defina usuário e senha fortes para o administrador");
    }
    if (!users.existsByUsername(username)) {
      users.save(new StaffUser(username, passwords.encode(password), Role.ADMIN));
    }
  }
}
