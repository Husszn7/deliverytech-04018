package com.deliverytech.delivery_api.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.requests.ProdutoDTO;
import com.deliverytech.delivery_api.dto.responses.ApiResponse;
import com.deliverytech.delivery_api.dto.responses.PagedResponse;
import com.deliverytech.delivery_api.dto.responses.ProdutoResponseDTO;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.service.ProdutoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/produtos", produces = "application/json")
@CrossOrigin(origins = "*")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANTE')")
    @PostMapping("/restaurante/{restauranteId}")
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> cadastrar(
            @PathVariable Long restauranteId,
            @RequestBody @Valid ProdutoDTO produto,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        produtoService.cadastrar(restauranteId, produto, usuarioLogado)
                ));
    }

    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<PagedResponse<ProdutoResponseDTO>> listarPorRestaurante(
            @PathVariable Long restauranteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        var pageResult = produtoService.listarPorRestaurante(restauranteId, pageable);

        return ResponseEntity.ok(new PagedResponse<>(pageResult));
    }

    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANTE')")
    @PatchMapping("/{produtoId}/disponibilidade")
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> toggle(
            @PathVariable Long produtoId,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        produtoService.toggleDisponibilidade(produtoId, usuarioLogado)
                )
        );
    }
}