package com.deliverytech.delivery_api.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.deliverytech.delivery_api.service.ProdutoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/produtos", produces = "application/json")
@Tag(name = "Produtos", description = "Endpoints para gerenciamento do cardápio.")
@CrossOrigin(origins = "*")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Operation(summary = "Cadastrar novo produto em um restaurante.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Produto criado."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurante não encontrado.")
    })
    @PostMapping("/restaurante/{restauranteId}")
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> cadastrar(
            @PathVariable Long restauranteId, 
            @RequestBody @Valid ProdutoDTO produto) {
        
        ProdutoResponseDTO resposta = produtoService.cadastrar(restauranteId, produto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(resposta));
    }

    @Operation(summary = "Listar produtos disponíveis de um restaurante (paginado).")
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<PagedResponse<ProdutoResponseDTO>> listarPorRestaurante(
            @PathVariable Long restauranteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        var pageResult = produtoService.listarPorRestaurante(restauranteId, pageable);
        return ResponseEntity.ok(new PagedResponse<>(pageResult));
    }

    @Operation(summary = "Alternar disponibilidade do produto.")
    @PatchMapping("/{produtoId}/disponibilidade")
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> toggleDisponibilidade(@PathVariable Long produtoId) {
        return ResponseEntity.ok(new ApiResponse<>(produtoService.toggleDisponibilidade(produtoId)));
    }
}
