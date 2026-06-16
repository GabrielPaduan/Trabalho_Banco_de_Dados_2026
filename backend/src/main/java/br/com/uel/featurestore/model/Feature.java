package br.com.uel.featurestore.model;

public class Feature {
    private int id;
    private String name;
    private String dataType;
    private String description;
    private int versionId;


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return this.dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVersionId() {
        return this.versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }
}
