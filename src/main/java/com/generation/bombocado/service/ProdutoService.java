package com.generation.bombocado.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import com.generation.bombocado.model.Produto;
import com.generation.bombocado.repository.CategoriaRepository;
import com.generation.bombocado.repository.ProdutoRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Produto> findAll() {
        return produtoRepository.findAll()
                .stream()
                .filter(this::isValidoParaCatalogo)
                .toList();
    }

    public Optional<Produto> findById(Long id) {
        return produtoRepository.findById(id);
    }

    public List<Produto> findAllByNome(String nome) {
        return produtoRepository.findAllByNomeContainingIgnoreCase(nome)
                .stream()
                .filter(this::isValidoParaCatalogo)
                .toList();
    }

    public Produto cadastrar(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo!");
        }

        validarCategoria(produto);
        definirNutriscore(produto);

        return produtoRepository.save(produto);
    }

    public Optional<Produto> atualizar(Produto produto) {
        if (produto == null || produto.getId() == null) {
            return Optional.empty();
        }

        if (produtoRepository.existsById(produto.getId())) {
            validarCategoria(produto);
            definirNutriscore(produto);
            return Optional.of(produtoRepository.save(produto));
        }

        return Optional.empty();
    }

    public boolean delete(Long id) {
        if (produtoRepository.existsById(id)) {
            produtoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private boolean isValidoParaCatalogo(Produto produto) {
        return produto != null 
                && produto.getDataValidade() != null 
                && !produto.getDataValidade().isBefore(LocalDate.now());
    }

    private void validarCategoria(Produto produto) {
        if (produto.getCategoria() != null && produto.getCategoria().getId() != null) {
            if (!categoriaRepository.existsById(produto.getCategoria().getId())) {
                throw new IllegalArgumentException("A Categoria informada não existe!");
            }
        }
    }

    private void definirNutriscore(Produto produto) {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            return;
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://br.openfoodfacts.org/cgi/search.pl?search_terms=" 
                    + produto.getNome() + "&search_simple=1&action=process&json=1";

            JsonNode root = restTemplate.getForObject(url, JsonNode.class);

            if (root != null && root.has("products") && root.get("products").isArray() && root.get("products").size() > 0) {
                JsonNode primeiroProduto = root.get("products").get(0);

                if (primeiroProduto.has("nutriscore_grade")) {
                    String grade = primeiroProduto.get("nutriscore_grade").asText();
                    if (grade != null && !grade.isBlank()) {
                        produto.setNutriscore(grade.toUpperCase());
                        return;
                    }
                }

                if (primeiroProduto.has("nutriscore_score")) {
                    int score = primeiroProduto.get("nutriscore_score").asInt();
                    produto.setNutriscore(converterScoreParaLetra(score));
                }
            }
        } catch (Exception e) {
        }
    }

    private String converterScoreParaLetra(int score) {
        if (score <= -1) {
            return "A";
        } else if (score <= 2) {
            return "B";
        } else if (score <= 10) {
            return "C";
        } else if (score <= 18) {
            return "D";
        } else {
            return "E";
        }
    }
}
