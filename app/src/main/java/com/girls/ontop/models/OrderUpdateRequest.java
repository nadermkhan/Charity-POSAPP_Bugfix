package com.girls.ontop.models;

import java.util.List;

public class OrderUpdateRequest {
    private int location_id;
    private int contact_id;


    private String discount_amount;
    private String discount_type;
    private float shipping_charges;
    private List<OrderProduct> products;

    private List<OrderPayment> payments;




    public OrderUpdateRequest(String location_id, int contact_id,float shipping_charges, List<OrderProduct> products,String discount_amount,String discount_type,List<OrderPayment> payments) {
        this.location_id = Integer.parseInt(location_id);
        this.contact_id = contact_id;
        this.shipping_charges = shipping_charges;
        this.discount_amount = discount_amount;
        this.products = products;
        this.payments = payments;
        this.discount_type = discount_type;
    }





    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProduct> products) {
        this.products = products;
    }
}
