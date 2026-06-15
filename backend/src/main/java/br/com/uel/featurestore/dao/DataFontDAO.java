package br.com.uel.featurestore.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.uel.featurestore.model.DataFont;

@Repository
public class DataFontDAO {
    private final JdbcTemplate jdbcTemplate;

    public DataFontDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createDataFont(DataFont data) {
        String sqlQuery = "INSERT INTO feature_store.fonte_de_dados (nome) VALUES (?)";
        jdbcTemplate.update(sqlQuery, data.getName());
    }

    public List<DataFont> getDataFonts() {
        String sqlQuery = "SELECT * FROM feature_store.fonte_de_dados";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            DataFont dataFont = new DataFont();
            dataFont.setId(rs.getInt("id"));
            dataFont.setName(rs.getString("nome"));
            return dataFont;
        });
    }
}
