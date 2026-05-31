package br.com.uel.featurestore.service;

import br.com.uel.featurestore.model.Usuario;
import br.com.uel.featurestore.dao.UsuarioDAO;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // Refere-se a implementação e validação das regras de negócio do sistema
public class UsuarioService {
    // Atributo para poder acessar funções da classe UsuarioDAO
    private final UsuarioDAO usuarioDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    // Funções que tratam os dados antes de serem enviados ao banco de dados
    public void cadastrarNovoUsuario(Usuario usuario) {
        // Validações de campos do usuário
        if (usuario.getCpf() == null || usuario.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do usuário não pode ser vazio!");
        }

        

        
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do usuário não pode ser vazio!");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("O email do usuário não pode ser vazio!");
        }

        if (usuario.getSenhaHash() == null || usuario.getSenhaHash().trim().isEmpty()) {
            throw new IllegalArgumentException("A senha do usuário não pode estar vazia!");
        }

        // Realização do hashing da senha baseando-se na classe ConfigSegurança.java
        String senha = usuario.getSenhaHash();
        String senhaCriptografada = passwordEncoder.encode(senha);
        usuario.setSenhaHash(senhaCriptografada);

        // Chama a DAO do usuário para executar a instrução SQL desejada
        usuarioDAO.salvarUsuario(usuario);
    }
}