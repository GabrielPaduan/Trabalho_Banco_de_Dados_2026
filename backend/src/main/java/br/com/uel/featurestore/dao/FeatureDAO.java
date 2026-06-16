package br.com.uel.featurestore.dao;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.uel.featurestore.model.Feature;

@Repository
public class FeatureDAO {
    private final JdbcTemplate jdbcTemplate;

    public FeatureDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertNewFeature(Feature featureData) {
        String sqlQuery = "INSERT INTO feature_store.feature (nome, tipo_dado, descricao, id_versao) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, featureData.getName(), featureData.getDataType(), featureData.getDescription(), featureData.getVersionId());
    }

    public List<Feature> getFeaturesByVersionId(int versionId) {
        String sqlQuery = "SELECT * FROM feature_store.feature WHERE id_versao = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            Feature feature = new Feature();
            feature.setId(rs.getInt("id"));
            feature.setName(rs.getString("nome"));
            feature.setDataType(rs.getString("tipo_dado"));
            feature.setDescription(rs.getString("descricao"));
            feature.setVersionId(versionId);
            return feature;
        }, versionId);
    }

    public Feature getFeatureById(int id) {
        String sqlQuery = "SELECT * FROM feature_store.feature WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> {
                Feature feature = new Feature();
                feature.setId(id);
                feature.setName(rs.getString("nome"));
                feature.setDataType(rs.getString("tipo_dado"));
                feature.setDescription(rs.getString("descricao"));
                feature.setVersionId(rs.getInt("id_versao"));
                return feature;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            return null; 
        }
    }

    public void updateFeature(int id, Feature featureData) {
        String sqlQuery = "UPDATE feature_store.feature SET nome = ?, tipo_dado = ?, descricao = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, featureData.getName(), featureData.getDataType(), featureData.getDescription(), id);
    }

    public void deleteFeature(int id) {
        String sqlQuery = "DELETE FROM feature_store.feature WHERE id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }
}
