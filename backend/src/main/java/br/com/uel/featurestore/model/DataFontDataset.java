package br.com.uel.featurestore.model;

public class DataFontDataset {
    private int id;
    private int datasetId;
    private int dataFontId;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id; 
    }

    public int getDatasetId() {
        return this.datasetId;
    }

    public void setDatasetId(int datasetId) {
        this.datasetId = datasetId; 
    }

    public int getDataFontId() {
        return this.dataFontId;
    }

    public void setDataFontId(int dataFontId) {
        this.dataFontId = dataFontId; 
    }
}
