package com.girls.ontop.models;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductionResponse {
    @SerializedName("data")
    private List<ProductionData> data;

    public List<ProductionData> getData() {
        return data;
    }

    public void setData(List<ProductionData> data) {
        this.data = data;
    }
}
