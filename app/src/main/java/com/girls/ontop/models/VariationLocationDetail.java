package com.girls.ontop.models;

public class VariationLocationDetail {
    private int id;
    private int product_id;
    private int product_variation_id;
    private int variation_id;
    private int location_id;
    private String qty_available;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return product_id;
    }

    public void setProductId(int product_id) {
        this.product_id = product_id;
    }

    public int getProductVariationId() {
        return product_variation_id;
    }

    public void setProductVariationId(int product_variation_id) {
        this.product_variation_id = product_variation_id;
    }

    public int getVariationId() {
        return variation_id;
    }

    public void setVariationId(int variation_id) {
        this.variation_id = variation_id;
    }

    public int getLocationId() {
        return location_id;
    }

    public void setLocationId(int location_id) {
        this.location_id = location_id;
    }

    public String getQtyAvailable() {
        return qty_available;
    }

    public void setQtyAvailable(String qty_available) {
        this.qty_available = qty_available;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(String updated_at) {
        this.updated_at = updated_at;
    }

}
