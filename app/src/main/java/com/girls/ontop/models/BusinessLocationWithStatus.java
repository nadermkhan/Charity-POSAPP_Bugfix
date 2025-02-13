package com.girls.ontop.models;

import java.util.List;

public class BusinessLocationWithStatus {

    private List<BusinessLocation> data;
    private String status;

    public BusinessLocationWithStatus(List<BusinessLocation> data, String status) {
        this.data = data;
        this.status = status;
    }

    public List<BusinessLocation> getData() {
        return data;
    }

    public void setData(List<BusinessLocation> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}