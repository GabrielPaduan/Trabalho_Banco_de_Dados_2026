package br.com.uel.featurestore.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.uel.featurestore.model.DataFontDataset;

@Repository
public class DataFontDatasetDAO {
    private final JdbcTemplate jdbcTemplate;

    public DataFontDatasetDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createRelationDataFontDataset(DataFontDataset data) {
        String sqlQuery = "INSERT INTO feature_store.fonte_dados_dataset (id_dataset, id_fonte_dados) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, data.getDatasetId(), data.getDataFontId());
    }

    public List<DataFontDataset> getRelationByDataFontIdDatasetId(int datasetId, int dataFontId) {
        String sqlQuery = "SELECT * FROM feature_store.fonte_dados_dataset WHERE id_dataset = ? AND id_fonte_dados = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            DataFontDataset dataFont = new DataFontDataset();
            dataFont.setId(rs.getInt("id"));
            dataFont.setDatasetId(datasetId);
            dataFont.setDataFontId(dataFontId);
            return dataFont;
        }, datasetId, dataFontId);
    }

    public List<DataFontDataset> getRelationDataFontDatasetByDatasetId(int id) {
        String sqlQuery = "SELECT * FROM feature_store.fonte_dados_dataset WHERE id_dataset = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            DataFontDataset dataFont = new DataFontDataset();
            dataFont.setId(rs.getInt("id"));
            dataFont.setDatasetId(id);
            dataFont.setDataFontId((rs.getInt("id_fonte_dados")));
            return dataFont;
        }, id);
    }
    
    public List<DataFontDataset> getRelationDataFontDatasetByDataFontId(int id) {
        String sqlQuery = "SELECT * FROM feature_store.fonte_dados_dataset WHERE id_fonte_dados = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            DataFontDataset dataFont = new DataFontDataset();
            dataFont.setId(rs.getInt("id"));
            dataFont.setDatasetId(rs.getInt("id_dataset"));
            dataFont.setDataFontId(id);
            return dataFont;
        }, id);
    }
}
