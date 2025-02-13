package com.girls.ontop.models;

import java.util.ArrayList;
import java.util.List;

public class Variation {
    private int id;
    private String name;
    private String sub_sku;
    private float default_purchase_price;
    private float default_sell_price;
    private float sell_price_inc_tax;
    private List<VariationLocationDetail> variation_location_details = new ArrayList<>();

    public Variation(int id, String name, String sub_sku, float default_purchase_price, float default_sell_price, float sell_price_inc_tax, List<VariationLocationDetail> variationLocationDetails) {
        this.id = id;
        this.name = name;
        this.sub_sku = sub_sku;
        this.default_purchase_price = default_purchase_price;
        this.default_sell_price = default_sell_price;
        this.sell_price_inc_tax = sell_price_inc_tax;
        this.variation_location_details = variationLocationDetails;
    }

    public List<VariationLocationDetail> getVariationLocationDetails() {
        return variation_location_details;
    }

    public void setVariationLocationDetails(List<VariationLocationDetail> variationLocationDetails) {
        this.variation_location_details = variationLocationDetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub_sku() {
        return sub_sku;
    }

    public void setSub_sku(String sub_sku) {
        this.sub_sku = sub_sku;
    }

    public float getDefault_purchase_price() {
        return default_purchase_price;
    }

    public void setDefault_purchase_price(float default_purchase_price) {
        this.default_purchase_price = default_purchase_price;
    }

    public float getDefault_sell_price() {
        return default_sell_price;
    }

    public void setDefault_sell_price(float default_sell_price) {
        this.default_sell_price = default_sell_price;
    }

    public float getSell_price_inc_tax() {
        return sell_price_inc_tax;
    }

    public void setSell_price_inc_tax(float sell_price_inc_tax) {
        this.sell_price_inc_tax = sell_price_inc_tax;
    }
}
