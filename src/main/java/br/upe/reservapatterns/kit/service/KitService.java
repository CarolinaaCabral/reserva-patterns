package br.upe.reservapatterns.kit.service;

import br.upe.reservapatterns.equipment.entity.Equipment;
import br.upe.reservapatterns.equipment.service.EquipmentService;
import br.upe.reservapatterns.exception.NotFoundException;
import br.upe.reservapatterns.kit.creation.KitBuilder;
import br.upe.reservapatterns.kit.creation.KitPrototype;
import br.upe.reservapatterns.kit.dto.CopyKitRequest;
import br.upe.reservapatterns.kit.dto.CreateKitRequest;
import br.upe.reservapatterns.kit.dto.KitItemRequest;
import br.upe.reservapatterns.kit.dto.KitResponse;
import br.upe.reservapatterns.kit.entity.Kit;
import br.upe.reservapatterns.kit.entity.KitItem;
import br.upe.reservapatterns.kit.repository.KitRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KitService {
  private final KitRepository kits;
  private final EquipmentService equipments;

  public KitService(KitRepository kits, EquipmentService equipments) {
    this.kits = kits;
    this.equipments = equipments;
  }

  @Transactional
  public KitResponse create(CreateKitRequest request) {
    KitBuilder builder = new KitBuilder(request.name())
            .description(request.description() == null ? "" : request.description());
    Set<Long> seen = new HashSet<>();
    for (KitItemRequest item : request.items()) {
      if (!seen.add(item.equipmentId())) {
        throw new IllegalArgumentException("Equipamento repetido");
      }
      Equipment equipment = equipments.get(item.equipmentId());
      builder.add(equipment, item.quantity());
    }
    return KitResponse.from(kits.saveAndFlush(builder.build()));
  }

  @Transactional
  public KitResponse copy(long kitId, CopyKitRequest request) {
    Kit copy = new KitPrototype(get(kitId)).copy();
    copy.rename(request.name());
    return KitResponse.from(kits.saveAndFlush(copy));
  }

  @Transactional(readOnly = true)
  public Kit get(long id) {
    return kits.findById(id).orElseThrow(() -> new NotFoundException("Kit não encontrado"));
  }

  @Transactional(readOnly = true)
  public KitResponse find(long id) {
    return KitResponse.from(get(id));
  }

  @Transactional(readOnly = true)
  public List<KitResponse> list() {
    return kits.findAll().stream().map(KitResponse::from).toList();
  }

  @Transactional
  public KitResponse changeQuantity(long kitId, long itemId, int quantity) {
    Kit kit = get(kitId);
    KitItem item = kit.getItems().stream().filter(line -> line.getId().equals(itemId))
            .findFirst().orElseThrow(() -> new NotFoundException("Item do kit não encontrado"));
    item.changeQuantity(quantity);
    return KitResponse.from(kit);
  }
}
