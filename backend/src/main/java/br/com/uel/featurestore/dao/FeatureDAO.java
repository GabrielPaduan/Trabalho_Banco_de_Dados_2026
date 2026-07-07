package br.com.uel.featurestore.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import br.com.uel.featurestore.model.Feature;

@Repository
public class FeatureDAO {
    private final JdbcTemplate jdbcTemplate;

    public FeatureDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Feature insertNewFeature(Feature featureData) {
        String sqlQuery = "INSERT INTO feature_store.feature (nome, tipo_dado, descricao, id_versao) VALUES (?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
  
        try {
            jdbcTemplate.update(connection -> {
                // Voltando para o array de string, mas certifique-se que a coluna no banco se chama exatamente "id"
                PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"}); 
                ps.setString(1, featureData.getName());
                ps.setString(2, featureData.getDataType());
                ps.setString(3, featureData.getDescription());
                ps.setInt(4, featureData.getVersionId());
                return ps;
            }, keyHolder);
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        Map<String, Object> keys = keyHolder.getKeys();

        if (keys != null && keys.get("id") != null) {
            featureData.setId(((Number) keys.get("id")).intValue());
        }
        
        return featureData;    
    }

    public List<Feature> getFeaturesByVersionId(int versionId) {
        String sqlQuery = "SELECT * FROM feature_store.feature WHERE id_versao = ? ORDER BY id ASC";
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
