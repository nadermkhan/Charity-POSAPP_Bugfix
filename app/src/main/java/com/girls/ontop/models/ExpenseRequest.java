package com.girls.ontop.models;

import java.util.List;

public class ExpenseRequest {
    int location_id;
    String final_total;
    int expense_category_id;
    int expense_sub_category_id;
    String additional_notes;
    private List<OrderPayment> payment;

    public ExpenseRequest(int location_id, String final_total, int expense_category_id, int expense_sub_category_id, String additional_notes, List<OrderPayment> payment) {
        this.location_id = location_id;
        this.final_total = final_total;
        this.expense_category_id = expense_category_id;
        this.expense_sub_category_id = expense_sub_category_id;
        this.additional_notes = additional_notes;
        this.payment = payment;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public String getFinal_total() {
        return final_total;
    }

    public void setFinal_total(String final_total) {
        this.final_total = final_total;
    }

    public int getExpense_category_id() {
        return expense_category_id;
    }

    public void setExpense_category_id(int expense_category_id) {
        this.expense_category_id = expense_category_id;
    }

    public int getExpense_sub_category_id() {
        return expense_sub_category_id;
    }

    public void setExpense_sub_category_id(int expense_sub_category_id) {
        this.expense_sub_category_id = expense_sub_category_id;
    }

    public String getAdditional_notes() {
        return additional_notes;
    }

    public void setAdditional_notes(String additional_notes) {
        this.additional_notes = additional_notes;
    }

    public List<OrderPayment> getPayment() {
        return payment;
    }

    public void setPayment(List<OrderPayment> payment) {
        this.payment = payment;
    }
}
