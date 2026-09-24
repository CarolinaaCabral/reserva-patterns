package br.upe.reservapatterns.kit.dto;

import br.upe.reservapatterns.kit.entity.KitItem;

public record KitItemResponse(Long id, Long equipmentId, String equipmentName, int quantity) {
  public static KitItemResponse from(KitItem item) {
    return new KitItemResponse(item.getId(), item.getEquipment().getId(),
            item.getEquipment().getName(), item.getQuantity());
  }
}