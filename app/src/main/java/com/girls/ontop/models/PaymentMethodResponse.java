package com.girls.ontop.models;

import java.util.Map;

public class PaymentMethodResponse {
    private Map<String, String> paymentMethods;

    public Map<String, String> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(Map<String, String> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }
}
