package br.upe.reservapatterns.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.upe.reservapatterns.booking.entity.Booking;
import br.upe.reservapatterns.booking.entity.BookingKind;
import br.upe.reservapatterns.booking.entity.BookingStatus;
import br.upe.reservapatterns.handover.creation.CounterHandoverFactory;
import br.upe.reservapatterns.handover.creation.CourierHandoverFactory;
import br.upe.reservapatterns.handover.entity.PickupTerms;
import br.upe.reservapatterns.handover.entity.ReturnTerms;
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
@DisplayName("Exercício #04 - AbstractFactory")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Execution(ExecutionMode.CONCURRENT)
class Req04AbstractFactoryTest {
  private final LocalDateTime start = LocalDateTime.of(2030, 4, 2, 9, 0);
  private final Booking booking = new Booking(new Kit("Aula", ""),
          new StaffUser("ana", "hash", Role.RESEARCHER), start, start.plusHours(4),
          BookingKind.LECTURE, BookingStatus.CONFIRMED);

  @Test
  void balcaoProduzParCompativelSemCusto() {
    CounterHandoverFactory factory = new CounterHandoverFactory();
    PickupTerms pickup = factory.pickup(booking, null);
    ReturnTerms returns = factory.returns(booking, null);
    assertEquals("COUNTER", pickup.method());
    assertEquals("COUNTER", returns.method());
    assertEquals(pickup.location(), returns.location());
    assertEquals(0, pickup.feeCents());
    assertEquals(booking.getEndsAt(), returns.dueAt());
  }

  @Test
  void mensageiroProduzParCompativelComEnderecoECusto() {
    CourierHandoverFactory factory = new CourierHandoverFactory();
    PickupTerms pickup = factory.pickup(booking, " Bloco B, sala 204 ");
    ReturnTerms returns = factory.returns(booking, " Bloco B, sala 204 ");
    assertEquals("COURIER", pickup.method());
    assertEquals("COURIER", returns.method());
    assertEquals("Bloco B, sala 204", pickup.location());
    assertEquals(pickup.location(), returns.location());
    assertEquals(1500, pickup.feeCents());
    assertEquals(booking.getEndsAt(), returns.dueAt());
  }

  @Test
  void mensageiroRequerEndereco() {
    assertThrows(IllegalArgumentException.class,
            () -> new CourierHandoverFactory().pickup(booking, " "));
  }
}
