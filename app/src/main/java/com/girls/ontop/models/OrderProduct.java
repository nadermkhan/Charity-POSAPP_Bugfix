package com.girls.ontop.models;

public class OrderProduct {
    private int product_id;
    private double quantity;
    private double unit_price;
    private String discount_type;
    private double discount_amount;
    private int variationId;

    public int getVariationId() {
        return variationId;
    }

    public void setVariationId(int variationId) {
        this.variationId = variationId;
    }

    public OrderProduct(int product_id, double quantity, double unit_price, int variationId,String discount_type,double discount_amount) {
        this.product_id = product_id;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.variationId = variationId;
        this.discount_type = discount_type;
        this.discount_amount = discount_amount;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }
}
