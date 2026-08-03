package com.generation.bombocado.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O Atributo Nome é Obrigatório!")
    @Size(min = 2, max = 100, message = "O Atributo Nome deve conter no mínimo 02 e no máximo 100 caracteres")
    @Column(length = 100)
    private String nome;

    @NotBlank(message = "O Atributo Descrição é Obrigatório!")
    @Size(min = 5, max = 500, message = "O Atributo Descrição deve conter no mínimo 05 e no máximo 500 caracteres")
    @Column(length = 500)
    private String descricao;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "O Atributo Preço é Obrigatório!")
    @Positive(message = "O Preço deve ser maior do que zero!")
    private BigDecimal preco;

    @Size(max = 5000, message = "O link da imagem não pode ultrapassar 5000 caracteres")
    @Column(length = 5000)
    private String imagem;

    private Integer tempoEntrega;

    @Size(max = 2, message = "O Nutri-score deve conter a letra correspondente (A, B, C, D ou E)")
    @Column(length = 2)
    private String nutriscore;

    @NotNull(message = "O Atributo Data de Validade é Obrigatório!")
    private LocalDate dataValidade;

    @ManyToOne
    @JsonIgnoreProperties("produto")
    private Categoria categoria;

    @ManyToOne
    @JsonIgnoreProperties("produto")
    private Usuario usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public Integer getTempoEntrega() {
        return tempoEntrega;
    }

    public void setTempoEntrega(Integer tempoEntrega) {
        this.tempoEntrega = tempoEntrega;
    }

    public String getNutriscore() {
        return nutriscore;
    }

    public void setNutriscore(String nutriscore) {
        this.nutriscore = nutriscore;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}