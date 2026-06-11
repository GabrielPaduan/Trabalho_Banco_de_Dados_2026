package br.com.uel.featurestore.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.uel.featurestore.model.Dataset;

@Repository
public class DatasetDao {
    private final JdbcTemplate jdbcTemplate;

    public DatasetDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveDataset(Dataset dataset) {
        String sqlQuery = "INSERT INTO feature_store.dataset (nome, descricao, data_criacao, cpf_usuario) VALUES(?, ?, CURRENT_DATE, ?)";
        jdbcTemplate.update(sqlQuery, dataset.getName(), dataset.getDesc(), dataset.getUserCPF());
    }

    public List<Dataset> getDataset(String userCpf, Boolean active) {
        String sqlQuery = "SELECT * FROM feature_store.dataset WHERE cpf_usuario = ? AND ativo = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            Dataset dataset = new Dataset();
            dataset.setId(rs.getInt("id"));
            dataset.setName(rs.getString("nome"));
            dataset.setDesc(rs.getString("descricao"));
            dataset.setCreatedDate(rs.getObject("data_criacao", LocalDate.class));
            dataset.setUserCPF(userCpf);
            dataset.setActive(rs.getBoolean("ativo"));
            return dataset;
        }, userCpf, active);
    }

    public void desactiveDataset(int id, boolean active) {
        String sqlQuery = "UPDATE feature_store.dataset SET ativo=? WHERE id=?";
        jdbcTemplate.update(sqlQuery, active, id);
    }

    public void updateDataset(Dataset dataset) {
        String sqlQuery = "UPDATE feature_store.dataset SET nome=?, descricao=? WHERE id=?";
        jdbcTemplate.update(sqlQuery, dataset.getName(), dataset.getDesc(), dataset.getId());
    }
}