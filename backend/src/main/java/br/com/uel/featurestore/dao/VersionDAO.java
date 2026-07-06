package br.com.uel.featurestore.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.uel.featurestore.model.Version;

@Repository
public class VersionDAO {
    private final JdbcTemplate jdbcTemplate;

    public VersionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createVersion(Version versionData) {
        System.out.println("Versão DAO: " + versionData);
        Integer idVersaoBaseParaOBanco = (versionData.getBaseVersionId() == 0) ? null : versionData.getBaseVersionId();
        String sqlQuery = "INSERT INTO feature_store.versao (arquivo_path, numero_versao, data_inclusao, id_dataset, id_versao_base, tamanho) VALUES (?, ?, CURRENT_DATE, ?, ?, ?)";
        
        jdbcTemplate.update(sqlQuery, versionData.getArchivePath(), versionData.getNumVersion(), versionData.getDatasetId(), idVersaoBaseParaOBanco, versionData.getSize());
    }

    public Version getVersionById(int versionId) {
        String sqlQuery = "SELECT * FROM feature_store.versao WHERE id = ?";
        List<Version> versions = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            Version newVersion = new Version();
            newVersion.setId(versionId);
            newVersion.setArchivePath(rs.getString("arquivo_path"));
            newVersion.setNumVersion(rs.getString("numero_versao"));
            newVersion.setCreatedDate(rs.getDate("data_inclusao"));
            newVersion.setDatasetId(rs.getInt("id_dataset"));
            newVersion.setBaseVersionId(rs.getInt("id_versao_base"));
            newVersion.setSize(BigInteger.valueOf(rs.getInt("tamanho")));
            return newVersion;
        }, versionId);

        return versions.isEmpty() ? null : versions.get(0);
    }

    public List<Version> getVersionByDataset(int datasetId) {
        String sqlQuery = "SELECT * FROM feature_store.versao WHERE id_dataset = ? ORDER BY numero_versao DESC";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            Version newVersion = new Version();
            newVersion.setId(rs.getInt("id"));
            newVersion.setArchivePath(rs.getString("arquivo_path"));
            newVersion.setNumVersion(rs.getString("numero_versao"));
            newVersion.setCreatedDate(rs.getDate("data_inclusao"));
            newVersion.setDatasetId(datasetId);
            newVersion.setBaseVersionId(rs.getInt("id_versao_base"));
            newVersion.setSize(BigInteger.valueOf(rs.getInt("tamanho")));
            return newVersion;
        }, datasetId);
    }

       public Version getRootVersion(int datasetId) {
        String sqlQuery = "SELECT * FROM feature_store.versao WHERE id_dataset = ? ORDER BY id ASC LIMIT 1";
        List<Version> versions = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            Version newVersion = new Version();
            newVersion.setId(rs.getInt("id"));
            newVersion.setArchivePath(rs.getString("arquivo_path"));
            newVersion.setNumVersion(rs.getString("numero_versao"));
            newVersion.setCreatedDate(rs.getDate("data_inclusao"));
            newVersion.setDatasetId(datasetId);
            newVersion.setBaseVersionId(rs.getInt("id_versao_base"));
            newVersion.setSize(BigInteger.valueOf(rs.getInt("tamanho")));
            return newVersion;
        }, datasetId);

        return versions.isEmpty() ? null : versions.get(0);
    }

     public Version getLastVersion(int datasetId) {
        String sqlQuery = "SELECT * FROM feature_store.versao WHERE id_dataset = ? ORDER BY id DESC LIMIT 1";
        return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> {
            Version newVersion = new Version();
            newVersion.setId(rs.getInt("id"));
            newVersion.setArchivePath(rs.getString("arquivo_path"));
            newVersion.setNumVersion(rs.getString("numero_versao"));
            newVersion.setCreatedDate(rs.getDate("data_inclusao"));
            newVersion.setDatasetId(datasetId);
            newVersion.setBaseVersionId(rs.getInt("id_versao_base"));
            newVersion.setSize(BigInteger.valueOf(rs.getInt("tamanho")));
            return newVersion;
        }, datasetId);
    }

    public List<Version> getVersionByBaseVersionId(int baseVersionId) {
        String sqlQuery = "SELECT * FROM feature_store.versao WHERE id_versao_base = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            Version newVersion = new Version();
            newVersion.setId(rs.getInt("id"));
            newVersion.setArchivePath(rs.getString("arquivo_path"));
            newVersion.setNumVersion(rs.getString("numero_versao"));
            newVersion.setCreatedDate(rs.getDate("data_inclusao"));
            newVersion.setDatasetId(rs.getInt("id_dataset"));
            newVersion.setBaseVersionId(baseVersionId);
            newVersion.setSize(BigInteger.valueOf(rs.getInt("tamanho")));
            return newVersion;
        }, baseVersionId);
    }

    public void deleteVersion(int idVersion) {
        String sqlQuery = "DELETE FROM feature_store.versao WHERE id = ?";
        jdbcTemplate.update(sqlQuery, idVersion);
    }
}
