package com.girls.ontop.models;

public class PaymentBody {
    private String contact_id;
    private double amount;
    private String method;
    private int account_id;

    public PaymentBody(String contact_id, double amount, String method, int account_id) {
        this.contact_id = contact_id;
        this.amount = amount;
        this.method = method;
        this.account_id = account_id;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }
}
