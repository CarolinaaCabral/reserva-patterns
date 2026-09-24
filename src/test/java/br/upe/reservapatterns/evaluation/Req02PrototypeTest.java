package br.upe.reservapatterns.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.upe.reservapatterns.equipment.entity.Equipment;
import br.upe.reservapatterns.kit.creation.KitPrototype;
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
@DisplayName("Exercício #02 - Prototype")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Execution(ExecutionMode.CONCURRENT)
class Req02PrototypeTest {
  @Test
  void duplicaItensMasCompartilhaEquipamentoDeCatalogo() {
    Equipment equipment = new Equipment("Câmera", 5);
    Kit original = new Kit("Original", "Fotografia");
    original.add(equipment, 2);
    Kit copy = new KitPrototype(original).copy();
    assertNotSame(original, copy);
    assertNull(copy.getId());
    assertEquals(original.getDescription(), copy.getDescription());
    assertEquals(original.getName(), copy.getName());
    assertNotSame(original.getItems().get(0), copy.getItems().get(0));
    assertSame(equipment, copy.getItems().get(0).getEquipment());
    copy.getItems().get(0).changeQuantity(3);
    copy.rename("Cópia");
    assertEquals(2, original.getItems().get(0).getQuantity());
    assertEquals("Original", original.getName());
  }

  @Test
  void exigeOrigem() {
    assertThrows(IllegalArgumentException.class, () -> new KitPrototype(null));
  }
}
