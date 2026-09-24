package br.upe.reservapatterns.staff.dto;

import br.upe.reservapatterns.staff.entity.StaffUser;
import br.upe.reservapatterns.staff.security.Role;

public record StaffResponse(Long id, String username, Role role) {
  public static StaffResponse from(StaffUser user) {
    return new StaffResponse(user.getId(), user.getUsername(), user.getRole());
  }
}
