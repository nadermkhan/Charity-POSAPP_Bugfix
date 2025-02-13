package com.girls.ontop.models;

public class ContactRequest {
    String type; // Required
    String supplier_business_name; // Required if type is supplier
    String first_name; // Required
    String mobile; // Required
    String shipping_address; // Optional
    String address_line_1; // Optional
    String contact_type;
    String customer_group_id;


    public ContactRequest(String type, String supplier_business_name, String first_name, String mobile, String shipping_address, String address_line_1,String contact_type,String customer_group_id) {
        this.type = type;
        this.supplier_business_name = supplier_business_name;
        this.first_name = first_name;
        this.mobile = mobile;
        this.shipping_address = shipping_address;
        this.address_line_1 = address_line_1;
        this.contact_type = contact_type;
        this.customer_group_id = customer_group_id;
    }

    // Getters and setters for each field
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSupplier_business_name() {
        return supplier_business_name;
    }

    public void setSupplier_business_name(String supplier_business_name) {
        this.supplier_business_name = supplier_business_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(String shipping_address) {
        this.shipping_address = shipping_address;
    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }
}
