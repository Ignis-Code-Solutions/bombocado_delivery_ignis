package com.generation.bombocado.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public class PedidoRequest {

    @NotEmpty(message = "O Pedido deve conter ao menos um item!")
    @Valid
    private List<ItemPedidoRequest> itens;

    public List<ItemPedidoRequest> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoRequest> itens) {
        this.itens = itens;
    }
}