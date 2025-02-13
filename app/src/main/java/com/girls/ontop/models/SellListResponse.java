package com.girls.ontop.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SellListResponse {

    // Change "sells" to "data" to match the response JSON structure
    @SerializedName("data")
    private List<Sell> sells;

    public List<Sell> getSells() {
        return sells;
    }

    public void setSells(List<Sell> sells) {
        this.sells = sells;
    }
}
