package br.upe.reservapatterns.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.upe.reservapatterns.booking.entity.Booking;
import br.upe.reservapatterns.booking.entity.BookingKind;
import br.upe.reservapatterns.booking.entity.BookingStatus;
import br.upe.reservapatterns.booking.repository.BookingRepository;
import br.upe.reservapatterns.handover.entity.HandoverMode;
import br.upe.reservapatterns.handover.entity.HandoverPlan;
import br.upe.reservapatterns.handover.entity.PickupTerms;
import br.upe.reservapatterns.handover.entity.ReturnTerms;
import br.upe.reservapatterns.handover.repository.HandoverRepository;
import br.upe.reservapatterns.kit.entity.Kit;
import br.upe.reservapatterns.kit.repository.KitRepository;
import br.upe.reservapatterns.staff.entity.StaffUser;
import br.upe.reservapatterns.staff.repository.StaffRepository;
import br.upe.reservapatterns.staff.security.Role;
import br.upe.reservapatterns.staff.security.TokenService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SecurityBaselineTest {
  @Autowired private MockMvc mvc;
  @Autowired private StaffRepository staff;
  @Autowired private KitRepository kits;
  @Autowired private BookingRepository bookings;
  @Autowired private HandoverRepository handovers;
  @Autowired private TokenService tokens;
  @Autowired private PasswordEncoder passwords;

  private StaffUser admin;
  private StaffUser assistant;
  private StaffUser alice;
  private StaffUser bob;

  @BeforeEach
  void users() {
    admin = staff.save(new StaffUser("admin", passwords.encode("StrongAdmin1"), Role.ADMIN));
    assistant = staff.save(new StaffUser("staff", passwords.encode("StrongStaff1"), Role.STAFF));
    alice = staff.save(new StaffUser("alice", passwords.encode("StrongAlice1"), Role.RESEARCHER));
    bob = staff.save(new StaffUser("bob", passwords.encode("StrongBob123"), Role.RESEARCHER));
  }

  private String bearer(StaffUser user) {
    return "Bearer " + tokens.issue(user);
  }

  @Test
  void exigeTokenEAutenticaSenhaCorreta() throws Exception {
    mvc.perform(get("/api/equipments")).andExpect(status().isUnauthorized());
    mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\":\"alice\",\"password\":\"incorrect\"}"))
            .andExpect(status().isUnauthorized());
    mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\":\"alice\",\"password\":\"StrongAlice1\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.token").isNotEmpty())
            .andExpect(jsonPath("$.tokenType").value("Bearer"));
    mvc.perform(get("/api/equipments").header("Authorization", "Bearer invalid"))
            .andExpect(status().isUnauthorized());
  }

  @Test
  void restringeCriacaoPorPapelESalvaApenasHash() throws Exception {
    String equipment = "{\"name\":\"Câmera\",\"totalUnits\":2}";
    mvc.perform(post("/api/equipments").header("Authorization", bearer(alice))
                    .contentType(MediaType.APPLICATION_JSON).content(equipment))
            .andExpect(status().isForbidden());
    mvc.perform(post("/api/kits").header("Authorization", bearer(alice))
                    .contentType(MediaType.APPLICATION_JSON).content("{}"))
            .andExpect(status().isForbidden());
    mvc.perform(post("/api/kits/1/copies").header("Authorization", bearer(alice))
                    .contentType(MediaType.APPLICATION_JSON).content("{}"))
            .andExpect(status().isForbidden());
    mvc.perform(post("/api/handovers").header("Authorization", bearer(alice))
                    .contentType(MediaType.APPLICATION_JSON).content("{}"))
            .andExpect(status().isForbidden());
    mvc.perform(post("/api/equipments").header("Authorization", bearer(assistant))
                    .contentType(MediaType.APPLICATION_JSON).content(equipment))
            .andExpect(status().isCreated());
    String newUser = "{\"username\":\"newuser\",\"password\":\"Passphrase123\","
            + "\"role\":\"RESEARCHER\"}";
    mvc.perform(post("/api/staff/users").header("Authorization", bearer(assistant))
                    .contentType(MediaType.APPLICATION_JSON).content(newUser))
            .andExpect(status().isForbidden());
    mvc.perform(post("/api/staff/users").header("Authorization", bearer(admin))
                    .contentType(MediaType.APPLICATION_JSON).content(newUser))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.passwordHash").doesNotExist());
    String hash = staff.findByUsername("newuser").orElseThrow().getPasswordHash();
    assertFalse(hash.equals("Passphrase123"));
    assertTrue(passwords.matches("Passphrase123", hash));
  }

  @Test
  void protegeReservaEEntregaDoOutroUsuarioEAprovacao() throws Exception {
    Kit kit = kits.save(new Kit("Aula", ""));
    LocalDateTime start = LocalDateTime.of(2030, 4, 2, 9, 0);
    Booking booking = bookings.save(new Booking(kit, alice, start, start.plusHours(4),
            BookingKind.LECTURE, BookingStatus.PENDING));
    mvc.perform(get("/api/bookings/{id}", booking.getId())
            .header("Authorization", bearer(bob))).andExpect(status().isForbidden());
    mvc.perform(get("/api/bookings/{id}", booking.getId())
            .header("Authorization", bearer(alice))).andExpect(status().isOk());
    mvc.perform(patch("/api/bookings/{id}/approve", booking.getId())
            .header("Authorization", bearer(alice))).andExpect(status().isForbidden());
    mvc.perform(patch("/api/bookings/{id}/approve", booking.getId())
            .header("Authorization", bearer(assistant))).andExpect(status().isOk());
    HandoverPlan plan = handovers.save(new HandoverPlan(booking, HandoverMode.COUNTER,
            new PickupTerms("COUNTER", "patternsoratório central", 0, "Identificação"),
            new ReturnTerms("COUNTER", "patternsoratório central", booking.getEndsAt(), "Devolver")));
    mvc.perform(get("/api/handovers/{id}", plan.getId())
            .header("Authorization", bearer(bob))).andExpect(status().isForbidden());
    mvc.perform(get("/api/handovers/{id}", plan.getId())
            .header("Authorization", bearer(alice))).andExpect(status().isOk());
  }
}
