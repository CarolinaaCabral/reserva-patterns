package br.upe.reservapatterns.staff.security;

import br.upe.reservapatterns.staff.entity.StaffUser;
import br.upe.reservapatterns.staff.repository.StaffRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
  private final StaffRepository repository;

  public DatabaseUserDetailsService(StaffRepository repository) {
    this.repository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    StaffUser user = repository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    return User.withUsername(user.getUsername())
            .password(user.getPasswordHash())
            .roles(user.getRole().name())
            .build();
  }
}
