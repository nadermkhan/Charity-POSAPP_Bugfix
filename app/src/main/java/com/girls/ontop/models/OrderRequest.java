package com.girls.ontop.models;

import java.util.List;

public class OrderRequest {
    private int location_id;
    private String contact_id;
    private String shipping_address;
    private String sale_note;
    private String discount_amount;
    private String discount_type;
    private float shipping_charges;
    private float packing_charge;
    private int is_direct_sale;
    private String shipping_status;

    private String pathaoarea;
    private String pathaocity;
    private String pathaozone;

    public String getShipping_status() {
        return shipping_status;
    }

    public void setShipping_status(String shipping_status) {
        this.shipping_status = shipping_status;
    }

    private List<OrderProduct> products;

    private List<OrderPayment> payments;

    private String packing_charge_type;

    public String getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(String shipping_address) {
        this.shipping_address = shipping_address;
    }


    public OrderRequest(String location_id, String contact_id, String shipping_address, String sale_note, float shipping_charges, List<OrderProduct> products, String discount_amount, String discount_type, List<OrderPayment> payments, float packing_charge, int is_direct_sale, String shipping_status, String packing_charge_type,String pathaoarea,String pathaocity,String pathaozone) {
        this.location_id = Integer.parseInt(location_id);
        this.contact_id = contact_id;
        this.shipping_address = shipping_address;
        this.sale_note = sale_note;
        this.shipping_charges = shipping_charges;
        this.discount_amount = discount_amount;
        this.products = products;
        this.payments = payments;
        this.discount_type = discount_type;
        this.packing_charge = packing_charge;
        this.is_direct_sale = is_direct_sale;
        this.shipping_status = shipping_status;
        this.packing_charge_type = "fixed";
        this.pathaoarea = pathaoarea;
        this.pathaocity = pathaocity;
        this.pathaozone = pathaozone;
    }





    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProduct> products) {
        this.products = products;
    }

    public OrderRequest(int is_direct_sale) {
        this.is_direct_sale = is_direct_sale;
    }

    public int getIs_direct_sale() {
        return is_direct_sale;
    }

    public void setIs_direct_sale(int is_direct_sale) {
        this.is_direct_sale = is_direct_sale;
    }
}
