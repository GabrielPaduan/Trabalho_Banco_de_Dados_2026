package br.com.uel.featurestore.dao;

import br.com.uel.featurestore.model.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDAO {
    // Ferramenta do Spring para execução do SQL
    private final JdbcTemplate jdbcTemplate;

    // Por meio desse construtor o Spring entrega a conexão com o banco de dados para uso
    public UsuarioDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Funções de manipulação no banco
    public void salvarUsuario(Usuario usuario) {
        // Query pura de manipulação no banco de dados
        String sqlQuery = "INSERT INTO feature_store.usuario (cpf, nome, email, senha_hash, data_criacao) VALUES (?, ?, ?, ?, CURRENT_DATE)";

        // O jdbcTemplate faz o trabalho de passar os dados e query para o banco de dados, atualizando-o.
        jdbcTemplate.update(sqlQuery, usuario.getCpf(), usuario.getNome(), usuario.getEmail(), usuario.getSenhaHash());
    }
}