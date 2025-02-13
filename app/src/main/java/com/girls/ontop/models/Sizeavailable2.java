package com.girls.ontop.models;

public class Sizeavailable2 {
    private String createdAt;
    private int id;
    private int locationId;
    private int productId;
    private Integer productVariationId;
    private String qtyAvailable;
    private String updatedAt;
    private int variationId;

    // No-argument constructor
    public Sizeavailable2() {
    }

    // All-argument constructor
    public Sizeavailable2(String createdAt, int id, int locationId, int productId, Integer productVariationId, String qtyAvailable, String updatedAt, int variationId) {
        this.createdAt = createdAt;
        this.id = id;
        this.locationId = locationId;
        this.productId = productId;
        this.productVariationId = productVariationId;
        this.qtyAvailable = qtyAvailable;
        this.updatedAt = updatedAt;
        this.variationId = variationId;
    }

    // Getters and Setters
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Integer getProductVariationId() {
        return productVariationId;
    }

    public void setProductVariationId(Integer productVariationId) {
        this.productVariationId = productVariationId;
    }

    public String getQtyAvailable() {
        return qtyAvailable;
    }

    public void setQtyAvailable(String qtyAvailable) {
        this.qtyAvailable = qtyAvailable;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getVariationId() {
        return variationId;
    }

    public void setVariationId(int variationId) {
        this.variationId = variationId;
    }
}