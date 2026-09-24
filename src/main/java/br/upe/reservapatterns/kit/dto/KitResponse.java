package br.upe.reservapatterns.kit.dto;

import br.upe.reservapatterns.kit.entity.Kit;
import java.util.List;

public record KitResponse(Long id, String name, String description, List<KitItemResponse> items) {
  public static KitResponse from(Kit kit) {
    return new KitResponse(kit.getId(), kit.getName(), kit.getDescription(),
            kit.getItems().stream().map(KitItemResponse::from).toList());
  }
}
