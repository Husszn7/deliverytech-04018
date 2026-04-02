package com.deliverytech.delivery_api.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="item_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantidade;

    @Column(name = "preco_unitario")
    private BigDecimal precoUnitario;


    private BigDecimal subtotal;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="produto_id")
    private Produto produto;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="pedido_id")
    private Pedido pedido;

}
