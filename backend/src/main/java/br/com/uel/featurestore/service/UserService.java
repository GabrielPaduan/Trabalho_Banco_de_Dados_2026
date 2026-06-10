package br.com.uel.featurestore.service;

import br.com.uel.featurestore.dao.UserDAO;
import br.com.uel.featurestore.model.User;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service // Refere-se a implementação e validação das regras de negócio do sistema
public class UserService {
    // Atributo para poder acessar funções da classe UsuarioDAO
    private final UserDAO userDAO;

    // Uso da classe referente a criptografia de dados (usada para a senha)
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // Funções que tratam os dados antes de serem enviados ao banco de dados
    public void createNewUser(User user) {
        // Validações de campos do usuário
        if (user.getCpf() == null || user.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do usuário não pode ser vazio!");
        }

        // Implementar a verificação de estrutura de CPF
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do usuário não pode ser vazio!");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("O email do usuário não pode ser vazio!");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("A senha do usuário não pode estar vazia!");
        }

        // Realização do hashing da senha baseando-se na classe ConfigSegurança.java
        String password = user.getPassword();
        String passwordHash = passwordEncoder.encode(password);
        user.setPassword(passwordHash);

        // Chama a DAO do usuário para executar a instrução SQL desejada
        userDAO.saveUser(user);
    }

    public List<User> getUsers() {
        return userDAO.getUser();
    }

    public void deleteUser(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("O valor de CPF não pode estar vazio!");
        }
        
        userDAO.removeUser(cpf);
    }

    public void updateUser(User user) {
        if (user.getCpf() == null || user.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O cpf não pode estar vazio!");
        }

        // Garante que o usuário só será atualizado caso ou o nome ou o email tenham algum tipo de alteração
        if ((user.getName().trim().isEmpty() || user.getName() == null) && (user.getEmail().trim().isEmpty() || user.getEmail() == null)) {
            throw new IllegalArgumentException("Para atualizar o usuário é necessário preencher no mínimo um campo!");    
        } 
        userDAO.updateUserDataByCPF(user);
    }

    public User getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O email não pode estar vazio!");
        }

        User user = userDAO.getUserByEmailDataBank(email); 
        return user;
    }

    public String userLogin(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O email precisa estar preenchido!");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("A senha precisa estar preenchida!");
        }

        // Busca os dados do usuário no banco
        User user = this.getUserByEmail(email);
        
        // Se o resultado for null, o usuário não existe e não realiza-se a validação de senha
        if (user == null) {
            throw new NoSuchElementException("O email está incorreto ou o usuário não existe!");
        }
        // Através do passwordEncoder verifica se a senha inserida é igual a cadastrada no banco
        boolean validation = passwordEncoder.matches(password, user.getPassword());

        if (validation == true) {
            return user.getName();
        } else {
            return null;
        }
    }
}
