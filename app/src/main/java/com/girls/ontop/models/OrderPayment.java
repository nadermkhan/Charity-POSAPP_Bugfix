package com.girls.ontop.models;

public class OrderPayment {
    String amount;
    int account_id;
    String method;

    public OrderPayment(String amount, int account_id, String method) {
        this.amount = amount;
        this.account_id = account_id;
        this.method = method;
    }

    public OrderPayment(String amount, int account_id) {
        this.amount = amount;
        this.account_id = account_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }
}
