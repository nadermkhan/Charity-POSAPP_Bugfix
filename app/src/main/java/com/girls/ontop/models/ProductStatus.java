package com.girls.ontop.models;

import java.util.List;

public class ProductStatus {

    private List<Product> data;
    private String status;

    // Default constructor
    public ProductStatus() {
    }

    // Constructor with parameters
    public ProductStatus(List<Product> data, String status) {
        this.data = data;
        this.status = status;
    }

    // Getter for data
    public List<Product> getData() {
        return data;
    }

    // Setter for data
    public void setData(List<Product> data) {
        this.data = data;
    }

    // Getter for status
    public String getStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(String status) {
        this.status = status;
    }
}
