package br.com.uel.featurestore.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import br.com.uel.featurestore.dao.DataFontDatasetDAO;
import br.com.uel.featurestore.model.DataFontDataset;

@Service
public class DataFontDatasetService {
    private final DataFontDatasetDAO dataFontDatasetDAO;

    public DataFontDatasetService(DataFontDatasetDAO dataFontDatasetDAO) {
        this.dataFontDatasetDAO = dataFontDatasetDAO;
    }

    public void createRelationDataFontDataset(DataFontDataset dataFontDataset) {
        if (dataFontDataset == null) {
            throw new IllegalArgumentException("A fonte de dados é inválida ou não existe");
        }

        if (dataFontDataset.getDataFontId() < 0 && dataFontDataset.getDatasetId() < 0) {
            throw new IllegalArgumentException("Os identificadores de dataset ou fonte de dados são inválidos!");
        }

        dataFontDatasetDAO.createRelationDataFontDataset(dataFontDataset);
        System.out.println("SERVICE: PROBLEMA!");
    }

    public List<DataFontDataset> getRelationDataFontDatasetByDatasetId(int datasetId) {
        if (datasetId < 0) {
            throw new IllegalArgumentException("O id do dataset é inválido");
        }

        List<DataFontDataset> listDataFontDataset = dataFontDatasetDAO.getRelationDataFontDatasetByDatasetId(datasetId);
        if (listDataFontDataset == null) {
            throw new NoSuchElementException("Não existe uma relação entre dataset e fonte de dados com esse id!");
        }

        return listDataFontDataset;
    }

    public List<DataFontDataset> getRelationDataFontDatasetByDataFontId(int dataFontId) {
        if (dataFontId < 0) {
            throw new IllegalArgumentException("O id da fonte de dados é inválido");
        }

        List<DataFontDataset> listDataFontDataset = dataFontDatasetDAO.getRelationDataFontDatasetByDataFontId(dataFontId);
        if (listDataFontDataset == null) {
            throw new NoSuchElementException("Não existe uma relação entre dataset e fonte de dados com esse id!");
        }

        return listDataFontDataset;
    }
}
