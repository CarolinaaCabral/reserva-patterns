package br.upe.reservapatterns.staff.entity;

import br.upe.reservapatterns.staff.security.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "staff_users")
public class StaffUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 80)
  private String username;

  @Column(nullable = false)
  private String passwordHash;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  protected StaffUser() {}

  public StaffUser(String username, String passwordHash, Role role) {
    if (username == null || username.isBlank() || passwordHash == null
            || passwordHash.isBlank() || role == null) {
      throw new IllegalArgumentException("Usuário inválido");
    }
    this.username = username;
    this.passwordHash = passwordHash;
    this.role = role;
  }

  public Long getId() { return id; }
  public String getUsername() { return username; }
  public String getPasswordHash() { return passwordHash; }
  public Role getRole() { return role; }
}
