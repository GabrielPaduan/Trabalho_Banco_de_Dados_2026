package br.com.uel.featurestore.model;

import java.time.LocalDateTime;

public class AccessLog {
    private Integer id;
    private Integer operationType; // 0 - Download | 1 - Upload (or Create) | 2 - Access
    private LocalDateTime dateTime; 
    private String userCPF;
    private Integer datasetID;

    public AccessLog() {}

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public Integer getOperationType() {
        return this.operationType;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public void setUserCPF(String userCPF) {
        this.userCPF = userCPF;
    }

    public String getUserCPF() {
        return this.userCPF;
    }

    public void setDatasetID(Integer datasetID) {
        this.datasetID = datasetID;
    }

    public Integer getDatasetID() {
        return this.datasetID;
    }
}
