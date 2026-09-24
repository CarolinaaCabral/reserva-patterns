package br.upe.reservapatterns.kit.controller;

import br.upe.reservapatterns.kit.dto.ChangeQuantityRequest;
import br.upe.reservapatterns.kit.dto.CopyKitRequest;
import br.upe.reservapatterns.kit.dto.CreateKitRequest;
import br.upe.reservapatterns.kit.dto.KitResponse;
import br.upe.reservapatterns.kit.service.KitService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kits")
public class KitController {
  private final KitService service;

  public KitController(KitService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public KitResponse create(@Valid @RequestBody CreateKitRequest request) {
    return service.create(request);
  }

  @PostMapping("/{id}/copies")
  @ResponseStatus(HttpStatus.CREATED)
  public KitResponse copy(@PathVariable long id, @Valid @RequestBody CopyKitRequest request) {
    return service.copy(id, request);
  }

  @PatchMapping("/{id}/items/{itemId}")
  public KitResponse changeQuantity(@PathVariable long id, @PathVariable long itemId,
                                    @Valid @RequestBody ChangeQuantityRequest request) {
    return service.changeQuantity(id, itemId, request.quantity());
  }

  @GetMapping("/{id}")
  public KitResponse find(@PathVariable long id) {
    return service.find(id);
  }

  @GetMapping
  public List<KitResponse> list() {
    return service.list();
  }
}
