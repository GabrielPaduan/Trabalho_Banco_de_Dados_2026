package br.com.uel.featurestore.model;

import java.util.Date;
import java.math.BigInteger;

public class Version {
    private int id;
    private String archivePath;
    private String numVersion;
    private Date createdDate;
    private int datasetId;
    private int baseVersionId;
    private BigInteger size;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setArchivePath(String archivePath) {
        this.archivePath = archivePath;
    }

    public String getArchivePath() {
        return this.archivePath;
    }

    public void setNumVersion(String numVersion) {
        this.numVersion = numVersion;
    }

    public String getNumVersion() {
        return this.numVersion;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setDatasetId(int datasetId) {
        this.datasetId = datasetId;
    }

    public int getDatasetId() {
        return this.datasetId;
    }

    public void setBaseVersionId(int baseVersionId) {
        this.baseVersionId = baseVersionId;
    }

    public int getBaseVersionId() {
        return this.baseVersionId;
    }

    public void setSize(BigInteger size) {
        this.size = size;
    }

    public BigInteger getSize() {
        return this.size;
    }
}
