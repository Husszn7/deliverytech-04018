package com.deliverytech.delivery_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;

@Service
public class ClienteService {
    private ClienteRepository repository;

    public ClienteService (ClienteRepository repository){
        this.repository = repository;
    }

    public Cliente cadastrar(Cliente cliente){
        if( repository.existsByEmail(cliente.getEmail()) ){
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        cliente.setAtivo(true);
        return repository.save(cliente);

    }


    public List<Cliente> listarAtivos(){
        return repository.findByAtivoTrue();
    }
}
