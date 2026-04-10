package com.deliverytech.delivery_api.controller;


import java.util.concurrent.TimeUnit;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.requests.ClienteDTO;
import com.deliverytech.delivery_api.dto.responses.ClienteResponseDTO;
import com.deliverytech.delivery_api.dto.responses.PagedResponse;
import com.deliverytech.delivery_api.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/clientes")
@Tag(name="Clientes", description="Endpoints para gerencimento de clientes.")
public class ClienteController {
    private ClienteService service;

    public ClienteController ( ClienteService service){
        this.service = service;
    }


    @Operation(summary="Cadastrar novo cliente.")
    @ApiResponses(
        value={
            @ApiResponse(responseCode="201", description="Clientes Cadastrado com sucesso."),
            @ApiResponse(responseCode="400", description="Erro de validação."),
        }
    )
    @PostMapping("/cadastrar")
    public ResponseEntity<ClienteResponseDTO> cadastrar(@Valid @RequestBody ClienteDTO dto ){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(dto));
    }

    @Operation(summary="Listar clientes ativos.")
    @ApiResponses(
        value={
            @ApiResponse(responseCode="200", description="Lista de clientes ativos retornado."),
            @ApiResponse(responseCode="404", description="Cliente não encontrado."),
        }
    )
    @GetMapping
    public ResponseEntity<PagedResponse<ClienteResponseDTO>> listarAtivos(
        @RequestParam(defaultValue="0") int page,
        @RequestParam(defaultValue="10")int size
    ){

        Pageable pageable = PageRequest.of(page, size);
        var pageResponse = new PagedResponse<>(service.listarAtivos(pageable));

        return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
        
        .header("Content-Type", "application/json").body(pageResponse);

    }

    @Operation(summary="Buscar cliente por Id.")
    @ApiResponses(
        value={
            @ApiResponse(responseCode="200", description="Cliente encontrado com sucesso."),
            @ApiResponse(responseCode="404", description="Cliente não encontrado com Id mencionado."),
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<com.deliverytech.delivery_api.dto.responses.ApiResponse<ClienteResponseDTO>> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok().header("Content-Type", "application/json").body(new com.deliverytech.delivery_api.dto.responses.ApiResponse<>( service.buscarPorId(id)));  
    }

    @Operation(summary="Ativar ou desativar cliente.")
    @ApiResponses(
        value={
            @ApiResponse(responseCode="200" ),
            @ApiResponse(responseCode="404", description="Cliente não encontrado com Id mencionado."),
        }
    )
    @PutMapping("/{id}/inativar-cliente")
    public  ClienteResponseDTO inativar(@PathVariable Long id){
        return service.inativar(id);
    }

    /* @PutMapping("/{id}/atualizar-dados-clientes")
    public Cliente atualizar(@PathVariable Long id, @RequestBody Cliente dados){
        return service.atualizar(id, dados);
    } */


}
