package com.girls.ontop.models;

public class DiscountItem {
    private Discount discount;
    private Integer discountId;
    private Integer variationId;

    // Constructor
    public DiscountItem() {
    }

    public DiscountItem(Discount discount, Integer discountId, Integer variationId) {
        this.discount = discount;
        this.discountId = discountId;
        this.variationId = variationId;
    }

    // Getters and Setters
    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public Integer getVariationId() {
        return variationId;
    }

    public void setVariationId(Integer variationId) {
        this.variationId = variationId;
    }
}