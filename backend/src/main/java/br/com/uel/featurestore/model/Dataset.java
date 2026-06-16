package br.com.uel.featurestore.model;

import java.time.LocalDate;

public class Dataset {
    private int id;
    private String name;
    private String description;
    private LocalDate createdDate;
    private String userCPF;
    private Boolean active;

    // Idealmente deveria ser criado uma entidade DTO para essa classe a fim de trabalhar com esse atributo
    private Double totalSize;

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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(LocalDate date) {
        this.createdDate = date;
    }

    public String getUserCPF() {
        return this.userCPF;
    }
    
    public void setUserCPF(String cpf) {
        this.userCPF = cpf;
    }

    public Boolean getActive() {
        return this.active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }

    public Double getTotalSize() {
        return this.totalSize;
    }
    
    public void setTotalSize(Double totalSize) {
        this.totalSize = totalSize;
    }

}
