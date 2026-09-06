package com.generation.bombocado.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PerfilAtualizacaoRequest {

    @NotBlank(message = "O Atributo Nome é Obrigatório!")
    private String nome;

    @NotBlank(message = "O atributo telefone é obrigatório")
    @Size(max = 15, message = "O telefone não pode ultrapassar 15 caracteres")
    private String telefone;

    @NotBlank(message = "O atributo endereço é obrigatório")
    @Size(min = 5, max = 255, message = "O endereço deve ter entre 5 e 255 caracteres")
    private String endereco;

    @Size(max = 5000, message = "O link da imagem não pode ser maior do que 5000 caracteres")
    private String imagem;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}