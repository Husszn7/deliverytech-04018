package com.deliverytech.delivery_api.controller;

import java.util.List;

import static org.mockito.Answers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.deliverytech.delivery_api.config.SecurityTestConfig;
import com.deliverytech.delivery_api.dto.requests.ClienteDTO;
import com.deliverytech.delivery_api.dto.responses.ClienteResponseDTO;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.security.JwtAuthenticationFilter;
import com.deliverytech.delivery_api.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.security.test.context.support.WithMockUser;


@WebMvcTest(
    controllers = ClienteController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JwtAuthenticationFilter.class
    )
)
@Import(SecurityTestConfig.class)
public class ClienteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService service;


    private ClienteDTO clienteValido(){
        ClienteDTO dto = new ClienteDTO();
        dto.setNome("João Silva");
        dto.setTelefone("(11)9999-9999");
        dto.setEndereco("Rua A, 123");
        return dto;
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    @DisplayName("Deve cadastrar o cliente e retornar 201 quando cadastrado.")
    void deveCadastrarCliente() throws Exception{
        ClienteDTO dto =  clienteValido();

        ClienteResponseDTO response = new ClienteResponseDTO();
        response.setNome("João Silva");

        when(service.cadastrar(any(ClienteDTO.class), eq("teste@gmail.com")))
            .thenReturn(response);

        mockMvc.perform(post("/api/clientes/cadastrar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o DTO for inválido.")
    void deveRetornar400QuandoDTOInvalido() throws Exception{
        ClienteDTO dto = new ClienteDTO();
        mockMvc.perform(post("/api/clientes/cadastrar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    void deveRetornarErroAoCadastrarClienteDuplicado() throws Exception {
        ClienteDTO dto =  clienteValido();

        when(service.cadastrar(any(ClienteDTO.class), eq("teste@gmail.com")))
        .thenThrow(new BusinessException("Cliente já cadastrado para este usuário."));

        mockMvc.perform(post("/api/clientes/cadastrar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isConflict());

    }


    @Test
    @WithMockUser(username = "teste@gmail.com")
    void deveListarClientesAtivos() throws Exception{
        ClienteResponseDTO cliente = new ClienteResponseDTO();
        cliente.setNome("Elaine Soares");

        List<ClienteResponseDTO> lista = List.of(cliente);

        Page<ClienteResponseDTO> pageResponse = new PageImpl<>(lista);

        when(service.listarAtivos(any(Pageable.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/api/clientes")
            .param("page", "0")
            .param("size", "10")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].nome").value("Elaine Soares"));

    }

    @Test
    @DisplayName("Deve buscar cliente por ID.")
    void deveBuscarPorId() throws Exception{
        ClienteResponseDTO response = new ClienteResponseDTO();
        response.setNome("Pedro Chaves");

        when(service.buscarPorId(1L)).thenReturn(response);

        mockMvc.perform(get("/api/clientes/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.nome").value("Pedro Chaves"));
    }

    @Test
    void deveRetornar404AoBuscarIdInexistente()throws Exception{
        Long idInexistente = 999L;

        when(service.buscarPorId(idInexistente))
        .thenThrow(new EntityNotFoundException("Cliente não encontrado."));

        mockMvc.perform(get("/api/clientes/{id}", idInexistente)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound());
    }


    @Test
    void deveInativarCliente() throws Exception{
        ClienteResponseDTO response = new ClienteResponseDTO();

        response.setId(1L);

        when(service.inativar(1L)).thenReturn(response);

        mockMvc.perform(put("/api/clientes/{id}/inativar-cliente", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L));
    }

}
