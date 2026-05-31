package br.com.uel.featurestore.model;
import java.time.LocalDate;

public class Usuario {
    // Definição de atributos
    private String cpf;
    private String nome;
    private String email;
    private String senhaHash;
    private LocalDate dataCriacao;

    // Criação de Construtor vazio (necessário com spring)
    public Usuario() {}

    // Funções Getters e Setters
    public String getCpf() {
        return this.cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() {
        return this.senhaHash;
    }

    public void setSenhaHash(String senha_hash) {
        this.senhaHash = senha_hash;
    }
}