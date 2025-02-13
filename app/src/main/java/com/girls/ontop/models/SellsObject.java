package com.girls.ontop.models;

import java.util.List;

public class SellsObject {
    private OrderRequest sells;

    public SellsObject(OrderRequest sells) {
        this.sells = sells;
    }

    public OrderRequest getSells() {
        return sells;
    }

    public void setSells(OrderRequest sells) {
        this.sells = sells;
    }
}
