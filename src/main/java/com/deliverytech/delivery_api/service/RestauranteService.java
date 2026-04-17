package com.deliverytech.delivery_api.service;


import java.math.BigDecimal;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.dto.requests.RestauranteDTO;
import com.deliverytech.delivery_api.dto.responses.RestauranteResponseDTO;
import com.deliverytech.delivery_api.enums.CategoriaRestaurante;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.repository.RestauranteRepository;

@Service
public class RestauranteService {

    private final RestauranteRepository repository;
    private final ModelMapper mapper;

    public RestauranteService(RestauranteRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public RestauranteResponseDTO cadastrar(RestauranteDTO dto, Usuario usuarioLogado) {

        if(usuarioLogado == null){
            throw new BusinessException("Usuário não autenticado.");
        }
        System.out.println("Usuario logado: " + usuarioLogado.getEmail() + " - Role: " + usuarioLogado.getRole());

        if (usuarioLogado.getRole().name().equals("RESTAURANTE"))  {
            if(repository.existsByUsuario_Id(usuarioLogado.getId())){
                throw new BusinessException("Você já possui um restaurante cadastrado.");
            }
        }

        if (repository.existsByNome(dto.getNome())) {
            throw new BusinessException("Restaurante com esse nome já cadastrado.");
        }

        CategoriaRestaurante categoriaEnum = CategoriaRestaurante.valueOf(dto.getCategoria().toUpperCase());

        Restaurante r = mapper.map(dto, Restaurante.class);
        r.setUsuario(usuarioLogado);
        r.setCategoria(categoriaEnum);
        r.setAtivo(true);
        r.setAvaliacao(BigDecimal.ZERO);
        
        Restaurante salvo = repository.save(r);
        return mapper.map(salvo, RestauranteResponseDTO.class);
    }

    public Page<RestauranteResponseDTO> listarAtivos(Pageable pageable) {
        return repository.findByAtivoTrue(pageable)
                .map(r -> mapper.map(r, RestauranteResponseDTO.class));
    }

    public Page<RestauranteResponseDTO> buscarPorCategoria(String categoria, Pageable pageable) {
        CategoriaRestaurante categoriaEnum;

        try{
            categoriaEnum = CategoriaRestaurante.valueOf(categoria.toUpperCase());
        }catch(IllegalArgumentException e){
            throw new BusinessException("Categoria inválida.");
        }

        return repository.findByCategoriaAndAtivoTrue(categoriaEnum, pageable)
                .map(r -> mapper.map(r, RestauranteResponseDTO.class));
    }

    public RestauranteResponseDTO buscarPorId(Long id) {
        Restaurante r = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado."));
        return mapper.map(r, RestauranteResponseDTO.class);
    }

    @Transactional
    public RestauranteResponseDTO toggle(Long id, Usuario usuarioLogado) {
        if(usuarioLogado == null){
            throw new BusinessException("Usuário não autenticado.");
        }

        boolean isAdmin = usuarioLogado.getRole().name().equals("ADMIN");
        boolean isRestaurante = usuarioLogado.getRole().name().equals("RESTAURANTE");

        if(!isAdmin && !isRestaurante){
            throw new BusinessException("Apenas ADMIN ou RESTAURANTE podem ativar/inativar um restaurante.");
        }

        Restaurante restaurante = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado."));
        
        if(isRestaurante){
            if(!restaurante.getUsuario().getId().equals(usuarioLogado.getId())){
                throw new BusinessException("Restaurantes só podem ativar/inativar seu próprio perfil.");
            }
        }

        restaurante.setAtivo(!restaurante.isAtivo());
        Restaurante salvo = repository.save(restaurante);

        return mapper.map(salvo, RestauranteResponseDTO.class);
    }
}