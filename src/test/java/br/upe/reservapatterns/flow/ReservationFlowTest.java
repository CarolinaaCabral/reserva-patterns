package br.upe.reservapatterns.flow;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.upe.reservapatterns.staff.entity.StaffUser;
import br.upe.reservapatterns.staff.repository.StaffRepository;
import br.upe.reservapatterns.staff.security.Role;
import br.upe.reservapatterns.staff.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/** Fluxo HTTP completo; passa quando os quatro exercícios forem implementados. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ReservationFlowTest {
  @Autowired private MockMvc mvc;
  @Autowired private StaffRepository staff;
  @Autowired private PasswordEncoder passwords;
  @Autowired private TokenService tokens;
  @Autowired private ObjectMapper json;

  private String send(String path, String auth, String body) throws Exception {
    return mvc.perform(post(path).header("Authorization", auth)
                    .contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
  }

  @Test
  void percorreCatalogoKitCopiaReservaAprovacaoEEntrega() throws Exception {
    StaffUser operator = staff.save(new StaffUser("operator", passwords.encode("operator-pass"),
            Role.STAFF));
    StaffUser researcher = staff.save(new StaffUser("researcher", passwords.encode("user-pass"),
            Role.RESEARCHER));
    String staffToken = "Bearer " + tokens.issue(operator);
    String userToken = "Bearer " + tokens.issue(researcher);
    String equipment = send("/api/equipments", staffToken,
            "{\"name\":\"Microscópio\",\"totalUnits\":2}");
    long equipmentId = json.readTree(equipment).get("id").asLong();
    String kit = send("/api/kits", staffToken,
            "{\"name\":\"Aula\",\"description\":\"Biologia\",\"items\":[{\"equipmentId\":"
                    + equipmentId + ",\"quantity\":2}]}");
    long kitId = json.readTree(kit).get("id").asLong();
    String copy = send("/api/kits/" + kitId + "/copies", staffToken,
            "{\"name\":\"Aula B\"}");
    long copyId = json.readTree(copy).get("id").asLong();
    mvc.perform(get("/api/kits/{id}", kitId).header("Authorization", userToken))
            .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Aula"));
    String bookingBody = "{\"kitId\":" + copyId
            + ",\"kind\":\"RESEARCH\",\"startsAt\":\"2030-04-02T09:00:00\"}";
    String booking = send("/api/bookings", userToken, bookingBody);
    long bookingId = json.readTree(booking).get("id").asLong();
    mvc.perform(post("/api/bookings").header("Authorization", userToken)
                    .contentType(MediaType.APPLICATION_JSON).content(bookingBody))
            .andExpect(status().isConflict());
    mvc.perform(patch("/api/bookings/{id}/approve", bookingId)
                    .header("Authorization", staffToken))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CONFIRMED"));
    String handover = send("/api/handovers", staffToken,
            "{\"bookingId\":" + bookingId
                    + ",\"mode\":\"COURIER\",\"destination\":\"Bloco B, sala 204\"}");
    long handoverId = json.readTree(handover).get("id").asLong();
    mvc.perform(get("/api/handovers/{id}", handoverId)
                    .header("Authorization", userToken)).andExpect(status().isOk())
            .andExpect(jsonPath("$.pickup.feeCents").value(1500))
            .andExpect(jsonPath("$.returns.location").value("Bloco B, sala 204"));
  }
}
