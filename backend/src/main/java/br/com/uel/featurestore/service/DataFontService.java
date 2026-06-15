package br.com.uel.featurestore.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import br.com.uel.featurestore.dao.DataFontDAO;
import br.com.uel.featurestore.model.DataFont;

@Service
public class DataFontService {
    private final DataFontDAO dataFontDao;

    public DataFontService(DataFontDAO dataFontDao) {
        this.dataFontDao = dataFontDao;
    }

    public void createDataFont(DataFont data) {
        if (data == null) {
            throw new IllegalArgumentException("Parâmetro inválido");
        }        

        if (data.getName() == null || data.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo nome não pode estar vazio!");
        }

        dataFontDao.createDataFont(data);
    }

    public List<DataFont> getDataFont() {
        List<DataFont> listDataFont = dataFontDao.getDataFonts();
        if (listDataFont == null) {
            throw new NoSuchElementException("Não foi encontrado nenhuma fonte de dados!");
        } 

        return listDataFont;
    }
}
