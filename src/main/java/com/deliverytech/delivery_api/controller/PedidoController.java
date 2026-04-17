package com.deliverytech.delivery_api.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.requests.PedidoDTO;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.service.PedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/pedidos", produces = "application/json")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @GetMapping("/meus")
    public ResponseEntity<?> meusPedidos(
            @AuthenticationPrincipal Usuario usuarioLogado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.meusPedidos(usuarioLogado, pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @PostMapping
    public ResponseEntity<?> criar(
            @RequestBody @Valid PedidoDTO dto,
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        return ResponseEntity.status(201)
                .body(service.criarPedido(dto, usuarioLogado));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuarioLogado
    ) {
        return ResponseEntity.ok(service.cancelarPedido(id, usuarioLogado));
    }
}