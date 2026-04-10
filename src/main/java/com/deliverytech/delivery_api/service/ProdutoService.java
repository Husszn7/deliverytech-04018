package com.deliverytech.delivery_api.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.dto.requests.ProdutoDTO;
import com.deliverytech.delivery_api.dto.responses.ProdutoResponseDTO;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;

import jakarta.transaction.Transactional;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final RestauranteRepository restauranteRepository;
    private final ModelMapper mapper;

    public ProdutoService(ProdutoRepository produtoRepository, RestauranteRepository restauranteRepository, ModelMapper mapper) {
        this.produtoRepository = produtoRepository;
        this.restauranteRepository = restauranteRepository;
        this.mapper = mapper;
    }

    private ProdutoResponseDTO returnResponseDTO(Produto p) {
        ProdutoResponseDTO dto = mapper.map(p, ProdutoResponseDTO.class);
        if (p.getRestaurante() != null) {
            dto.setRestauranteId(p.getRestaurante().getId());
        }
        return dto;
    }

    @Transactional
    public ProdutoResponseDTO cadastrar(Long restauranteId, ProdutoDTO produto) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não localizado."));
        
        if (!restaurante.isAtivo()) {
            throw new BusinessException("Restaurante inativo. Não é possível cadastrar produtos.");
        }

        Produto novoProduto = mapper.map(produto, Produto.class);
        novoProduto.setDisponivel(true);
        novoProduto.setRestaurante(restaurante);
        
        return returnResponseDTO(produtoRepository.save(novoProduto));
    }

    public Page<ProdutoResponseDTO> listarPorRestaurante(Long restauranteId, Pageable pageable) {
        if (!restauranteRepository.existsById(restauranteId)) {
            throw new EntityNotFoundException("Restaurante não localizado.");
        }
        return produtoRepository.findByRestauranteIdAndDisponivelTrue(restauranteId, pageable)
                .map(this::returnResponseDTO);
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto p = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
        return returnResponseDTO(p);
    }

    @Transactional
    public ProdutoResponseDTO toggleDisponibilidade(Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
        produto.setDisponivel(!produto.isDisponivel());
        return returnResponseDTO(produtoRepository.save(produto));
    }
}
