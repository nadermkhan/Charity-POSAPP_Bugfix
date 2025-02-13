package com.girls.ontop.models;

public class UpdateShippingStatusRequest {
    private int id;
    private String shipping_status;
    private String note;

    public UpdateShippingStatusRequest(int id, String shipping_status,String note) {
        this.id = id;
        this.shipping_status = shipping_status;
        this.note = note;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShipping_status() {
        return shipping_status;
    }

    public void setShipping_status(String shipping_status) {
        this.shipping_status = shipping_status;
    }
}
