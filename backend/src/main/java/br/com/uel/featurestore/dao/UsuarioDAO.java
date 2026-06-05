package br.com.uel.featurestore.dao;

import br.com.uel.featurestore.model.Usuario;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cglib.core.Local;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDAO {
    // Ferramenta do Spring para execução do SQL
    private final JdbcTemplate jdbcTemplate;

    // Por meio desse construtor o Spring entrega a conexão com o banco de dados para uso
    public UsuarioDAO(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Funções de manipulação no banco
    public void salvarUsuario(Usuario usuario) {
        // Query pura de manipulação no banco de dados
        String sqlQuery = "INSERT INTO feature_store.usuario (cpf, nome, email, senha_hash, data_criacao) VALUES (?, ?, ?, ?, CURRENT_DATE)";

        // O jdbcTemplate faz o trabalho de passar os dados e query para o banco de dados, atualizando-o.
        jdbcTemplate.update(sqlQuery, usuario.getCpf(), usuario.getNome(), usuario.getEmail(), usuario.getSenhaHash());
    }

    public List<Usuario> obterUsuario() {
        String sqlQuery = "SELECT cpf, nome, email, data_criacao FROM feature_store.usuario";
        // Itera-se sobre todas as linhas retornadas do banco
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            Usuario usuario = new Usuario();
            usuario.setCpf(rs.getString("cpf"));
            usuario.setNome(rs.getString("nome"));
            usuario.setEmail(rs.getString("email"));
            usuario.setDataCriacao(rs.getObject("data_criacao", LocalDate.class));
            return usuario;
        });
    }

    public void removerUsuario(String cpf) {
        String sqlQuery = "DELETE FROM feature_store.usuario WHERE cpf = ?";
        jdbcTemplate.update(sqlQuery, cpf);
    }

    public void atualizarDadosUsuarioPorCPF(Usuario usuario) {
        String sqlQuery = "UPDATE feature_store.usuario set nome=?, email=? WHERE cpf = ?";
        jdbcTemplate.update(sqlQuery, usuario.getNome(), usuario.getEmail(), usuario.getCpf());
    }

    public Usuario pegarUsuarioPorEmailBanco(String email) {
        String sqlQuery = "SELECT cpf, nome, email, senha_hash, data_criacao FROM feature_store.usuario WHERE email = ?";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> {
                Usuario usuario = new Usuario();
                usuario.setCpf(rs.getString("cpf"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(email);
                usuario.setSenhaHash(rs.getString("senha_hash"));
                usuario.setDataCriacao(rs.getObject("data_criacao", LocalDate.class));
                return usuario;
            },
            email
        );
    }

    public void redefinirSenhaBanco(String email, String senha) {
        String sqlQuery = "UPDATE feature_store.usuario SET senha_hash = ? WHERE email = ?";
        jdbcTemplate.update(sqlQuery, senha, email);
    }
}