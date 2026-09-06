package com.generation.bombocado.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.bombocado.dto.ItemPedidoRequest;
import com.generation.bombocado.dto.PedidoRequest;
import com.generation.bombocado.model.ItemPedido;
import com.generation.bombocado.model.Pedido;
import com.generation.bombocado.model.Produto;
import com.generation.bombocado.model.Usuario;
import com.generation.bombocado.repository.PedidoRepository;
import com.generation.bombocado.repository.ProdutoRepository;
import com.generation.bombocado.repository.UsuarioRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Pedido criarPedido(String usuarioLogado, PedidoRequest request) {

        Usuario usuario = usuarioRepository.findByUsuario(usuarioLogado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!"));

        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemPedidoRequest itemRequest : request.getItens()) {

            Produto produto = produtoRepository.findById(itemRequest.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Produto de id " + itemRequest.getProdutoId() + " não encontrado!"));

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(itemRequest.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());

            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(itemRequest.getQuantidade()));
            valorTotal = valorTotal.add(subtotal);

            itens.add(item);
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus("CRIADO");
        pedido.setValorTotal(valorTotal);

        itens.forEach(item -> item.setPedido(pedido));
        pedido.setItens(itens);

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarHistorico(String usuarioLogado) {

        Usuario usuario = usuarioRepository.findByUsuario(usuarioLogado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!"));

        return pedidoRepository.findAllByUsuario_IdOrderByDataPedidoDesc(usuario.getId());
    }

    public Optional<Pedido> buscarPorId(Long id, String usuarioLogado) {

        Optional<Pedido> pedido = pedidoRepository.findById(id);

        if (pedido.isPresent() && !pedido.get().getUsuario().getUsuario().equals(usuarioLogado)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Esse pedido não pertence a você!");
        }

        return pedido;
    }
}