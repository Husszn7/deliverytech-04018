package com.deliverytech.delivery_api.service;

import java.math.BigDecimal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.dto.requests.ItemPedidoDTO;
import com.deliverytech.delivery_api.dto.requests.PedidoDTO;
import com.deliverytech.delivery_api.dto.responses.PedidoResponseDTO;
import com.deliverytech.delivery_api.enums.StatusPedido;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.ItemPedido;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final RestauranteRepository restauranteRepository;
    private final ProdutoRepository produtoRepository;
    private final ModelMapper mapper;

    public PedidoService(
            PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository,
            RestauranteRepository restauranteRepository,
            ProdutoRepository produtoRepository,
            ModelMapper mapper) {

        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.restauranteRepository = restauranteRepository;
        this.produtoRepository = produtoRepository;
        this.mapper = mapper;
    }

    private PedidoResponseDTO toDTO(Pedido pedido) {
        return mapper.map(pedido, PedidoResponseDTO.class);
    }


    @Transactional
    public PedidoResponseDTO criarPedido(PedidoDTO dto, Usuario usuarioLogado) {

        if (usuarioLogado == null) {
            throw new BusinessException("Usuário não autenticado.");
        }
        Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail())
                .orElseThrow(() -> new BusinessException("Cliente não encontrado para este usuário."));

        if (!cliente.isAtivo()) {
            throw new BusinessException("Cliente inativo.");
        }

        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado."));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setEnderecoEntrega(dto.getEnderecoEntrega());

        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoDTO itemDTO : dto.getItens()) {

            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

            if (!produto.isDisponivel()) {
                throw new BusinessException("Produto indisponível: " + produto.getNome());
            }

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());

            BigDecimal subtotal = produto.getPreco()
                    .multiply(BigDecimal.valueOf(itemDTO.getQuantidade()));

            item.setSubtotal(subtotal);

            pedido.getItens().add(item);
            total = total.add(subtotal);
        }

        pedido.setValorTotal(total);

        return toDTO(pedidoRepository.save(pedido));
    }

    private void validarDonoPedido(Pedido pedido, Usuario usuarioLogado) {
        if (!pedido.getCliente().getEmail().equals(usuarioLogado.getEmail())) {
            throw new BusinessException("Você não tem permissão para acessar este pedido.");
        }
    }

    @Transactional
    public PedidoResponseDTO cancelarPedido(Long pedidoId, Usuario usuarioLogado) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));

        validarDonoPedido(pedido, usuarioLogado);

        if (pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new BusinessException("Pedido entregue não pode ser cancelado.");
        }

        pedido.setStatus(StatusPedido.CANCELADO);

        return toDTO(pedidoRepository.save(pedido));
    }

    public Page<PedidoResponseDTO> meusPedidos(Usuario usuarioLogado, Pageable pageable) {

        Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail())
                .orElseThrow(() -> new BusinessException("Cliente não encontrado."));

        return pedidoRepository.buscarItensPorClientes(cliente.getId(), pageable)
                .map(this::toDTO);
    }
}