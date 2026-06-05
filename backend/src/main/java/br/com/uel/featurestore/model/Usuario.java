package br.com.uel.featurestore.model;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Usuario {
    // Definição de atributos
    private String cpf;
    private String nome;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // A fim de segurança, essa JsonProperty foi implementada para evitar que o backend envie no JSON o atributo senha para o banco de dados, mas permite que o frontend envie a senha para o backend, permitindo - por exemplo - o cadastro
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

    public LocalDate getDataCriacao() {
        return this.dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
