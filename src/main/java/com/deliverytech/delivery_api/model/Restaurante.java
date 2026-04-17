package com.deliverytech.delivery_api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.deliverytech.delivery_api.enums.CategoriaRestaurante;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "restaurante")
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private CategoriaRestaurante categoria;

    private String endereco;

    private String telefone;

    /*     @Column(name = "taxa_entrega")
    private BigDecimal taxaEntrega; */

    private BigDecimal avaliacao;

    private boolean ativo;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="usuario_id", nullable=false)
    private Usuario usuario;

    @OneToMany(mappedBy= "restaurante", fetch=FetchType.LAZY)
    private List<Produto> produtos = new ArrayList<>();

    @OneToMany(mappedBy= "restaurante", fetch=FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();
}
