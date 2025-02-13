package com.girls.ontop.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class Product implements Parcelable {
    private Variation selectedVariation;
    private String variationname;
    private int id;
    private String name;
    private String sku;
    private String image_url;

    private int quantity;



    private float PriceInFloat;
    private float DiscountType;
    private float Discount;
    private float Stock;

    public float getStock() {
        return Stock;
    }

    public void setStock(float stock) {
        Stock = stock;
    }

    private int variationid;

    public int getVariationid() {
        return variationid;
    }

    public void setVariationid(int variationid) {
        this.variationid = variationid;
    }

    public Variation getSelectedVariation() {
        return selectedVariation;
    }

    public void setSelectedVariation(Variation selectedVariation) {
        this.selectedVariation = selectedVariation;
    }

    public String getVariationname() {
        return variationname;
    }

    public void setVariationname(String variationname) {
        this.variationname = variationname;
    }



    protected Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        sku = in.readString();
        image_url = in.readString();
        quantity = in.readInt();
        PriceInFloat = in.readFloat();
        DiscountType = in.readFloat();
        Discount = in.readFloat();
        variationid = in.readInt();
        variationname = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public float getPriceInFloat() {
        return PriceInFloat;
    }

    public void setPriceInFloat(float priceInFloat) {
        PriceInFloat = priceInFloat;
    }

    public float getDiscountType() {
        return DiscountType;
    }

    public void setDiscountType(float discountType) {
        DiscountType = discountType;
    }

    public float getDiscount() {
        return Discount;
    }

    public void setDiscount(float discount) {
        Discount = discount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private List<ProductVariation> product_variations;
    private List<ProductLocation> productLocations;


    public Product(int id, String name, String sku, String image_url, List<ProductVariation> product_variations, List<ProductLocation> productLocations) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.image_url = image_url;
        this.product_variations = product_variations;
        this.productLocations = productLocations;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public List<ProductVariation> getProduct_variations() {
        return product_variations;
    }

    public void setProduct_variations(List<ProductVariation> product_variations) {
        this.product_variations = product_variations;
    }

    public List<ProductLocation> getProductLocations() {
        return productLocations;
    }

    public void setProductLocations(List<ProductLocation> productLocations) {
        this.productLocations = productLocations;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(sku);
        parcel.writeString(image_url);
        parcel.writeInt(quantity);
        parcel.writeFloat(PriceInFloat);
        parcel.writeFloat(DiscountType);
        parcel.writeFloat(Discount);
        parcel.writeInt(variationid);
        parcel.writeString(variationname);
    }
}

