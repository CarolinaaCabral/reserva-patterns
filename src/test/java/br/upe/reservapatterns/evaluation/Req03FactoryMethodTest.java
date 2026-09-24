package br.upe.reservapatterns.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.upe.reservapatterns.booking.creation.LectureBookingCreator;
import br.upe.reservapatterns.booking.creation.ResearchBookingCreator;
import br.upe.reservapatterns.booking.entity.Booking;
import br.upe.reservapatterns.booking.entity.BookingKind;
import br.upe.reservapatterns.booking.entity.BookingStatus;
import br.upe.reservapatterns.equipment.entity.Equipment;
import br.upe.reservapatterns.kit.entity.Kit;
import br.upe.reservapatterns.staff.entity.StaffUser;
import br.upe.reservapatterns.staff.security.Role;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Exercício #03 - FactoryMethod")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Execution(ExecutionMode.CONCURRENT)
class Req03FactoryMethodTest {
  private final LocalDateTime start = LocalDateTime.of(2030, 4, 2, 9, 0);
  private final StaffUser researcher = new StaffUser("ana", "hash", Role.RESEARCHER);

  private Kit kit() {
    Kit kit = new Kit("Pesquisa", "Biologia");
    kit.add(new Equipment("Microscópio", 5), 2);
    return kit;
  }

  @Test
  void aulaConfirmaQuatroHorasERegistraItens() {
    Kit kit = kit();
    Booking booking = new LectureBookingCreator().open(kit, researcher, start);
    assertEquals(BookingKind.LECTURE, booking.getKind());
    assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    assertEquals(start.plusHours(4), booking.getEndsAt());
    assertSame(researcher, booking.getRequester());
    assertEquals(2, booking.getItems().get(0).getQuantity());
    assertNotSame(kit.getItems().get(0), booking.getItems().get(0));
  }

  @Test
  void pesquisaPendePorQuarentaEOitoHorasComSnapshot() {
    Kit kit = kit();
    Booking booking = new ResearchBookingCreator().open(kit, researcher, start);
    assertEquals(BookingKind.RESEARCH, booking.getKind());
    assertEquals(BookingStatus.PENDING, booking.getStatus());
    assertEquals(start.plusHours(48), booking.getEndsAt());
    kit.getItems().get(0).changeQuantity(3);
    assertEquals(2, booking.getItems().get(0).getQuantity());
  }

  @Test
  void rejeitaKitSemItens() {
    assertThrows(IllegalArgumentException.class,
            () -> new LectureBookingCreator().open(new Kit("Vazio", ""), researcher, start));
  }
}
