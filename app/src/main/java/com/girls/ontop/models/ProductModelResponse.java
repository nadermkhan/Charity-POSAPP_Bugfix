package com.girls.ontop.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProductModelResponse {
    @SerializedName("data")
    private List<ProductModel> data;

    public List<ProductModel> getData() {
        return data;
    }

    public void setData(List<ProductModel> data) {
        this.data = data;
    }

    public static class ProductModel  {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        @SerializedName("type")
        private String type;

        private List<ProductVariation> product_variations;

        public ProductModel(int id, String name, String type, List<ProductVariation> productVariations) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.product_variations = productVariations;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<ProductVariation> getProductVariations() {
            return product_variations;
        }

        public void setProductVariations(List<ProductVariation> productVariations) {
            this.product_variations = productVariations;
        }
    }
}
