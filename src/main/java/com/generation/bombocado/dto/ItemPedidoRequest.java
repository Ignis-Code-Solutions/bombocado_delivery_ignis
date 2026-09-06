package com.generation.bombocado.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ItemPedidoRequest {

    @NotNull(message = "O Atributo Produto é Obrigatório!")
    private Long produtoId;

    @NotNull(message = "O Atributo Quantidade é Obrigatório!")
    @Positive(message = "A Quantidade deve ser maior do que zero!")
    private Integer quantidade;

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}