package br.upe.reservapatterns.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.upe.reservapatterns.equipment.entity.Equipment;
import br.upe.reservapatterns.kit.creation.KitBuilder;
import br.upe.reservapatterns.kit.entity.Kit;
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
@DisplayName("Exercício #01 - Builder")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Execution(ExecutionMode.CONCURRENT)
class Req01BuilderTest {
  private final Equipment microscope = new Equipment("Microscópio", 3);

  @Test
  void criaKitComNomeDescricaoEItem() {
    Kit kit = new KitBuilder("Aula").description("Turma de biologia")
            .add(microscope, 2).build();
    assertEquals("Aula", kit.getName());
    assertEquals("Turma de biologia", kit.getDescription());
    assertEquals(1, kit.getItems().size());
    assertSame(microscope, kit.getItems().get(0).getEquipment());
    assertEquals(2, kit.getItems().get(0).getQuantity());
  }

  @Test
  void cadaBuildTemNovosItens() {
    KitBuilder builder = new KitBuilder("Aula").add(microscope, 1);
    Kit first = builder.build();
    Kit second = builder.build();
    assertNotSame(first, second);
    assertNotSame(first.getItems().get(0), second.getItems().get(0));
    second.getItems().get(0).changeQuantity(2);
    assertEquals(1, first.getItems().get(0).getQuantity());
  }

  @Test
  void rejeitaVazioExcessoERepeticao() {
    assertThrows(IllegalArgumentException.class, () -> new KitBuilder("Aula").build());
    KitBuilder builder = new KitBuilder("Aula").add(microscope, 1);
    assertThrows(IllegalArgumentException.class, () -> builder.add(microscope, 1));
    assertThrows(IllegalArgumentException.class,
            () -> new KitBuilder("Aula").add(microscope, 4));
  }
}
