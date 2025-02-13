package com.girls.ontop.utils;

public interface iAdapterListeners {
    void onIncrement(int pos);
    void onDecrement(int pos);
    void updateDue(Float due);
    void updateDiscount(Float totalDiscount);
    void updateSubTotal(Float let);
}
