package br.com.uel.featurestore.model;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    // Definição de atributos
    private String cpf;
    private String name;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // A fim de segurança, essa JsonProperty foi implementada para evitar que o backend envie no JSON o atributo senha para o banco de dados, mas permite que o frontend envie a senha para o backend, permitindo - por exemplo - o cadastro
    private String password;
    private LocalDate createdDate;

    // Criação de Construtor vazio (necessário com spring)
    public User() {}

    // Funções Getters e Setters
    public String getCpf() {
        return this.cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}
