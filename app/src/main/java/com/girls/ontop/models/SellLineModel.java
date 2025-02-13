package com.girls.ontop.models;

public class SellLineModel {
    int id;
    int product_id;
    int variation_id;
    int quantity;
    float unit_price;
    float unit_price_before_discount,line_discount_amount;

    public SellLineModel(int id, int product_id, int variation_id, int quantity, float unit_price,float unit_price_before_discount, float line_discount_amount) {
        this.id = id;
        this.product_id = product_id;
        this.variation_id = variation_id;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.unit_price_before_discount = unit_price_before_discount;
        this.line_discount_amount = line_discount_amount;
    }

    public float getUnit_price_before_discount() {
        return unit_price_before_discount;
    }

    public void setUnit_price_before_discount(float unit_price_before_discount) {
        this.unit_price_before_discount = unit_price_before_discount;
    }

    public float getLine_discount_amount() {
        return line_discount_amount;
    }

    public void setLine_discount_amount(float line_discount_amount) {
        this.line_discount_amount = line_discount_amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getVariation_id() {
        return variation_id;
    }

    public void setVariation_id(int variation_id) {
        this.variation_id = variation_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(int unit_price) {
        this.unit_price = unit_price;
    }
}
