package com.girls.ontop.models;

public class SellsUpdateObject {
    private OrderUpdateRequest sells;

    public SellsUpdateObject(OrderUpdateRequest sells) {
        this.sells = sells;
    }

    public OrderUpdateRequest getSells() {
        return sells;
    }

    public void setSells(OrderUpdateRequest sells) {
        this.sells = sells;
    }
}
