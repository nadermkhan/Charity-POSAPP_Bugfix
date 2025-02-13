package com.girls.ontop.models;

import java.util.List;

public class ExpenseResponse {
    private List<Expense> data;

    public List<Expense> getData() {
        return data;
    }

    public void setData(List<Expense> data) {
        this.data = data;
    }
}
