package br.upe.reservapatterns.equipment.dto;

import br.upe.reservapatterns.equipment.entity.Equipment;

public record EquipmentResponse(Long id, String name, int totalUnits) {
  public static EquipmentResponse from(Equipment equipment) {
    return new EquipmentResponse(equipment.getId(), equipment.getName(),
            equipment.getTotalUnits());
  }
}
