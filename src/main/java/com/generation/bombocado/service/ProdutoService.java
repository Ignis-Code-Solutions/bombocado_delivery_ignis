package com.generation.bombocado.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.generation.bombocado.model.Produto;
import com.generation.bombocado.repository.CategoriaRepository;
import com.generation.bombocado.repository.ProdutoRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;
    
    // Injeção das credenciais lidas das variáveis de ambiente
    @Value("${openfoodfacts.username:}")
    private String offUsername;

    @Value("${openfoodfacts.password:}")
    private String offPassword;

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
            produto.setNutriscore("-");
            return;
        }

        try {
            RestTemplate restTemplate = new RestTemplate();

            // 1. Cabeçalhos: User-Agent e Accept
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.USER_AGENT, "BombocadoApp/1.0 (contato@bombocado.com)");
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            if (offUsername != null && !offUsername.isBlank() && offPassword != null && !offPassword.isBlank()) {
                headers.setBasicAuth(offUsername, offPassword);
            }

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            // 2. Endpoint moderno v2
            String url = UriComponentsBuilder.fromUriString("https://world.openfoodfacts.org/api/v2/search")
                    .queryParam("search_terms", produto.getNome())
                    .queryParam("fields", "product_name,nutriscore_grade,nutrition_grades,nutriscore_score")
                    .queryParam("page_size", 1)
                    .encode()
                    .toUriString();

            System.out.println("[OpenFoodFacts] Consultando URL: " + url);

            // 3. Recebe a resposta como String.class (evita o erro com classe abstrata)
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getBody() != null) {
                // Converte a String para a árvore JsonNode usando ObjectMapper
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());

                if (root.has("products") && root.get("products").isArray() && root.get("products").size() > 0) {
                    JsonNode primeiroProduto = root.get("products").get(0);

                    // Prioridade 1: nutriscore_grade
                    if (primeiroProduto.hasNonNull("nutriscore_grade")) {
                        String grade = primeiroProduto.get("nutriscore_grade").asText();
                        if (!grade.isBlank() && !grade.equalsIgnoreCase("unknown")) {
                            produto.setNutriscore(grade.toUpperCase());
                            System.out.println("[OpenFoodFacts] Encontrado via nutriscore_grade: " + produto.getNutriscore());
                            return;
                        }
                    }

                    // Prioridade 2: nutrition_grades
                    if (primeiroProduto.hasNonNull("nutrition_grades")) {
                        String grade = primeiroProduto.get("nutrition_grades").asText();
                        if (!grade.isBlank() && !grade.equalsIgnoreCase("unknown")) {
                            produto.setNutriscore(grade.toUpperCase());
                            System.out.println("[OpenFoodFacts] Encontrado via nutrition_grades: " + produto.getNutriscore());
                            return;
                        }
                    }

                    // Prioridade 3: cálculo via score numérico
                    if (primeiroProduto.hasNonNull("nutriscore_score")) {
                        int score = primeiroProduto.get("nutriscore_score").asInt();
                        produto.setNutriscore(converterScoreParaLetra(score));
                        System.out.println("[OpenFoodFacts] Calculado via score: " + produto.getNutriscore());
                        return;
                    }
                }
            }

            System.out.println("[OpenFoodFacts] Nenhum produto com Nutri-Score encontrado para: " + produto.getNome());
            produto.setNutriscore("-");

        } catch (Exception e) {
            System.err.println("[OpenFoodFacts] Erro na requisição: " + e.getMessage());
            produto.setNutriscore("-");
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
