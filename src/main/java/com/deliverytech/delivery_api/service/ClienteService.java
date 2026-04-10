package com.deliverytech.delivery_api.service;


import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.dto.requests.ClienteDTO;
import com.deliverytech.delivery_api.dto.responses.ClienteResponseDTO;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;

import jakarta.transaction.Transactional;

@Service
public class ClienteService {


    private final ClienteRepository repository;

    private final ModelMapper mapper;

    public ClienteService (ClienteRepository repository, ModelMapper mapper){
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public ClienteResponseDTO cadastrar(ClienteDTO dto){
        if( repository.existsByEmail(dto.getEmail()) ){
            throw new BusinessException("E-mail já cadastrado.");
        }
        Cliente cliente = mapper.map(dto, Cliente.class);
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
