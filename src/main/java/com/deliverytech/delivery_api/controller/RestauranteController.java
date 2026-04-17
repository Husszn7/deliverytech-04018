package com.deliverytech.delivery_api.controller;

import java.net.URI;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deliverytech.delivery_api.dto.requests.RestauranteDTO;
import com.deliverytech.delivery_api.dto.responses.ApiResponse;
import com.deliverytech.delivery_api.dto.responses.PagedResponse;
import com.deliverytech.delivery_api.dto.responses.RestauranteResponseDTO;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.service.RestauranteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/restaurantes", produces = "application/json")
@CrossOrigin(origins = "*")
public class RestauranteController {

    private final RestauranteService service;

    public RestauranteController(RestauranteService service) {
        this.service = service;
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANTE')")
    @PostMapping
    public ResponseEntity<ApiResponse<RestauranteResponseDTO>> cadastrar(
            @Valid @RequestBody RestauranteDTO dados,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        RestauranteResponseDTO response = service.cadastrar(dados, usuarioLogado);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse<>(response));
    }

    
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<RestauranteResponseDTO>> toggle(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        return ResponseEntity.ok(
                new ApiResponse<>(service.toggle(id, usuarioLogado))
        );
    }

    @GetMapping
    public ResponseEntity<PagedResponse<RestauranteResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                new PagedResponse<>(service.listarAtivos(pageable))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RestauranteResponseDTO>> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(service.buscarPorId(id))
        );
    }

    @GetMapping("/categoria")
    public ResponseEntity<PagedResponse<RestauranteResponseDTO>> buscarPorCategoria(
            @RequestParam String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                new PagedResponse<>(service.buscarPorCategoria(categoria, pageable))
        );
    }

    
}