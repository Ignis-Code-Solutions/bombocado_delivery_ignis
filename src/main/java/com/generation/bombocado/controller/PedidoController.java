package com.generation.bombocado.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.bombocado.dto.PedidoRequest;
import com.generation.bombocado.model.Pedido;
import com.generation.bombocado.service.PedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> criar(@Valid @RequestBody PedidoRequest pedidoRequest,
            Authentication authentication) {

        Pedido pedido = pedidoService.criarPedido(authentication.getName(), pedidoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @GetMapping("/meus-pedidos")
    public ResponseEntity<List<Pedido>> meusPedidos(Authentication authentication) {
        return ResponseEntity.ok(pedidoService.listarHistorico(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getById(@PathVariable Long id, Authentication authentication) {
        return pedidoService.buscarPorId(id, authentication.getName())
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.notFound().build());
    }
}