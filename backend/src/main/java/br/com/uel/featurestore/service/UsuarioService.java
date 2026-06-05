package br.com.uel.featurestore.service;

import br.com.uel.featurestore.model.Usuario;
import br.com.uel.featurestore.dao.UsuarioDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service // Refere-se a implementação e validação das regras de negócio do sistema
public class UsuarioService {
    // Atributo para poder acessar funções da classe UsuarioDAO
    private final UsuarioDAO usuarioDAO;

    // Uso da classe referente a criptografia de dados (usada para a senha)
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

        // Implementar a verificação de estrutura de CPF
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

    public List<Usuario> pegarUsuarios() {
        return usuarioDAO.obterUsuario();
    }

    public void excluirUsuario(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("O valor de CPF não pode estar vazio!");
        }
        
        usuarioDAO.removerUsuario(cpf);
    }

    public void atualizarUsuario(Usuario usuario) {
        if (usuario.getCpf() == null || usuario.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O cpf não pode estar vazio!");
        }

        // Garante que o usuário só será atualizado caso ou o nome ou o email tenham algum tipo de alteração
        if ((usuario.getNome().trim().isEmpty() || usuario.getNome() == null) && (usuario.getEmail().trim().isEmpty() || usuario.getEmail() == null)) {
            throw new IllegalArgumentException("Para atualizar o usuário é necessário preencher no mínimo um campo!");    
        } 
        usuarioDAO.atualizarDadosUsuarioPorCPF(usuario);
    }

    public Usuario pegarUsuarioPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O email não pode estar vazio!");
        }

        Usuario usuario = usuarioDAO.pegarUsuarioPorEmailBanco(email); 
        return usuario;
    }

    public boolean loginUsuario(String email, String senha) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O email precisa estar preenchido!");
        }

        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("A senha precisa estar preenchida!");
        }

        // Busca os dados do usuário no banco
        Usuario usuario = usuarioDAO.pegarUsuarioPorEmailBanco(email);
        // Se o resultado for null, o usuário não existe e não realiza-se a validação de senha
        if (usuario == null) {
            throw new NoSuchElementException("O email está incorreto ou o usuário não existe!");
        }

        // Através do passwordEncoder verifica se a senha inserida é igual a cadastrada no banco
        boolean validacao = passwordEncoder.matches(senha, usuario.getSenhaHash());

        return validacao;
    }

    public void redefinirSenha(String email, String senha) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O email não pode estar vazio!");
        }

        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("A senha não pode estar vazia!");
        }
        
        Usuario usuario = usuarioDAO.pegarUsuarioPorEmailBanco(email);

        if (usuario == null) {
            throw new NoSuchElementException("O email está incorreto ou o usuário não existe no banco!");
        }

        String senhaHash = passwordEncoder.encode(senha);

        usuarioDAO.redefinirSenhaBanco(email, senhaHash);
    }
}
