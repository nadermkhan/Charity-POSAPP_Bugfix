package com.girls.ontop.models;

public class PaymentLineModel {
    int id;
    int transaction_id;
    int account_id;
    float amount;

    public PaymentLineModel(int id, int transaction_id, int account_id, float amount) {
        this.id = id;
        this.transaction_id = transaction_id;
        this.account_id = account_id;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
