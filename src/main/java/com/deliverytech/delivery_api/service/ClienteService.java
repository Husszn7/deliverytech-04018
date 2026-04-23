package com.deliverytech.delivery_api.service;


import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.dto.requests.ClienteDTO;
import com.deliverytech.delivery_api.dto.responses.ClienteResponseDTO;
import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class ClienteService {


    private final ClienteRepository repository;
    private final UsuarioRepository usuarioRepository;

    private final ModelMapper mapper;

    public ClienteService (ClienteRepository repository, ModelMapper mapper, UsuarioRepository usuarioRepository){
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ClienteResponseDTO cadastrar(ClienteDTO dto, String email) {

        if (email == null) {
            throw new BusinessException("Usuário não autenticado.");
        }

        Usuario usuarioLogado = usuarioRepository.findByEmail(email)
        .orElseThrow(()-> new BusinessException("Usuário autenticado não encontrado no banco de dado."));

        if (usuarioLogado.getRole() != Role.CLIENTE && usuarioLogado.getRole() != Role.ADMIN) {
            throw new BusinessException("Apenas CLIENTE ou ADMIN podem criar perfil de cliente.");
        }

    
        if (repository.existsByUsuario_Id(usuarioLogado.getId())) {
            throw new BusinessException("Cliente já cadastrado para este usuário.");
        }

        Cliente cliente = mapper.map(dto, Cliente.class);

        cliente.setUsuario(usuarioLogado);
        cliente.setEmail(usuarioLogado.getEmail());
        cliente.setAtivo(true);

        Cliente salvo = repository.save(cliente);

        return mapper.map(salvo, ClienteResponseDTO.class);
    }

    public Page<ClienteResponseDTO> listarAtivos(Pageable pageable){
        return repository.findByAtivoTrue(pageable)
        .map(clientes -> mapper.map(clientes, ClienteResponseDTO.class));
    }
    

    public ClienteResponseDTO buscarPorId(Long id){
        Cliente cliente =  repository.findById(id)
        .orElseThrow(()-> new EntityNotFoundException("Cliente não encontrado."));

        return mapper.map(cliente, ClienteResponseDTO.class);
    }


    public ClienteResponseDTO inativar(Long id){
        Cliente cliente =  repository.findById(id)
        .orElseThrow(()-> new EntityNotFoundException("Cliente não encontrado."));
        cliente.setAtivo(!cliente.isAtivo());
        Cliente salvo = repository.save(cliente);
        return mapper.map(salvo, ClienteResponseDTO.class);
    }

    /* public Cliente atualizar(Long id, Cliente dados){
        Cliente cliente = buscarPorId(id);
        cliente.setNome(dados.getNome());
        cliente.setEmail(dados.getEmail());
        cliente.setTelefone(dados.getTelefone());
        cliente.setEndereco(dados.getEndereco());
        return repository.save(cliente);
    } */


    
}
