package br.com.uel.featurestore.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import br.com.uel.featurestore.model.User;

@Repository
public class UserDAO {
    // Ferramenta do Spring para execução do SQL
    private final JdbcTemplate jdbcTemplate;

    // Por meio desse construtor o Spring entrega a conexão com o banco de dados para uso
    public UserDAO(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Funções de manipulação no banco
    public void saveUser(User user) {
        // Query pura de manipulação no banco de dados
        String sqlQuery = "INSERT INTO feature_store.usuario (cpf, nome, email, senha_hash, data_criacao) VALUES (?, ?, ?, ?, CURRENT_DATE)";

        // O jdbcTemplate faz o trabalho de passar os dados e query para o banco de dados, atualizando-o.
        jdbcTemplate.update(sqlQuery, user.getCpf(), user.getName(), user.getEmail(), user.getPassword());
    }

    public List<User> getUser() {
        String sqlQuery = "SELECT cpf, nome, email, data_criacao FROM feature_store.usuario";
        // Itera-se sobre todas as linhas retornadas do banco
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            User user = new User();
            user.setCpf(rs.getString("cpf"));
            user.setName(rs.getString("nome"));
            user.setEmail(rs.getString("email"));
            user.setCreatedDate(rs.getObject("data_criacao", LocalDate.class));
            return user;
        });
    }

    public void removeUser(String cpf) {
        String sqlQuery = "DELETE FROM feature_store.usuario WHERE cpf = ?";
        jdbcTemplate.update(sqlQuery, cpf);
    }

    public void updateUserDataByCPF(User user) {
        String sqlQuery = "UPDATE feature_store.usuario set nome=?, email=? WHERE cpf = ?";
        jdbcTemplate.update(sqlQuery, user.getName(), user.getEmail(), user.getCpf());
    }

    public User getUserByEmailDataBank(String email) {
        String sqlQuery = "SELECT cpf, nome, email, senha_hash, data_criacao FROM feature_store.usuario WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> {
                    User user = new User();
                    user.setCpf(rs.getString("cpf"));
                    user.setName(rs.getString("nome"));
                    user.setEmail(email);
                    user.setPassword(rs.getString("senha_hash"));
                    user.setCreatedDate(rs.getObject("data_criacao", LocalDate.class));
                    return user;
                },
                email
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void redifinyPasswordDataBank(String email, String password) {
        String sqlQuery = "UPDATE feature_store.usuario SET senha_hash = ? WHERE email = ?";
        jdbcTemplate.update(sqlQuery, password, email);
    }
}