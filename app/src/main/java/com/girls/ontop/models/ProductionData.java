package com.girls.ontop.models;
import com.google.gson.annotations.SerializedName;

public class ProductionData {
    @SerializedName("id")
    private int id;

    @SerializedName("transaction_date")
    private String transactionDate;

    @SerializedName("created_by")
    private String createdBy;

    @SerializedName("mfg_production_cost")
    private String mfgProductionCost;

    @SerializedName("ref_no")
    private String refNo;

    @SerializedName("location_name")
    private String locationName;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("final_total")
    private String finalTotal;

    @SerializedName("sub_unit_name")
    private String subUnitName;

    @SerializedName("base_unit_multiplier")
    private String baseUnitMultiplier;

    @SerializedName("unit_name")
    private String unitName;

    @SerializedName("mfg_is_final")
    private String mfgIsFinal;

    @SerializedName("production_cost")
    private String productionCost;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTransactionDate() { return transactionDate; }
    public void setTransactionDate(String transactionDate) { this.transactionDate = transactionDate; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getMfgProductionCost() { return mfgProductionCost; }
    public void setMfgProductionCost(String mfgProductionCost) { this.mfgProductionCost = mfgProductionCost; }

    public String getRefNo() { return refNo; }
    public void setRefNo(String refNo) { this.refNo = refNo; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public String getFinalTotal() { return finalTotal; }
    public void setFinalTotal(String finalTotal) { this.finalTotal = finalTotal; }

    public String getSubUnitName() { return subUnitName; }
    public void setSubUnitName(String subUnitName) { this.subUnitName = subUnitName; }

    public String getBaseUnitMultiplier() { return baseUnitMultiplier; }
    public void setBaseUnitMultiplier(String baseUnitMultiplier) { this.baseUnitMultiplier = baseUnitMultiplier; }

    public String getUnitName() { return unitName; }
    public void setUnitName(String unitName) { this.unitName = unitName; }

    public String getMfgIsFinal() { return mfgIsFinal; }
    public void setMfgIsFinal(String mfgIsFinal) { this.mfgIsFinal = mfgIsFinal; }

    public String getProductionCost() { return productionCost; }
    public void setProductionCost(String productionCost) { this.productionCost = productionCost; }
}
