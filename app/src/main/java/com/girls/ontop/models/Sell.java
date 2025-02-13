package com.girls.ontop.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Sell {

    @SerializedName("id") // Corresponds to "id" in the JSON response
    private int id;

    @SerializedName("invoice_no") // Corresponds to "invoice_no" in the JSON response
    private String invoiceNo;

    @SerializedName("transaction_date") // Corresponds to "transaction_date" in the JSON response
    private String transactionDate;

    @SerializedName("total_before_tax") // Corresponds to "total_before_tax" in the JSON response
    private String totalBeforeTax;

    @SerializedName("final_total") // Corresponds to "total_before_tax" in the JSON response
    private String final_total;

    @SerializedName("location_id") // Corresponds to "total_before_tax" in the JSON response
    private String location_id;

    @SerializedName("sell_line_note") // Corresponds to "total_before_tax" in the JSON response
    private String sell_line_note;

    @SerializedName("discount_type") // Corresponds to "total_before_tax" in the JSON response
    private String discount_type;

    @SerializedName("discount_amount") // Corresponds to "total_before_tax" in the JSON response
    private String discount_amount;

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public void setFinal_total(String final_total) {
        this.final_total = final_total;
    }

    public String getSell_line_note() {
        return sell_line_note;
    }

    public void setSell_line_note(String sell_line_note) {
        this.sell_line_note = sell_line_note;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    @SerializedName("tax_amount") // Corresponds to "tax_amount" in the JSON response
    private String taxAmount;

    @SerializedName("payment_status") // Corresponds to "payment_status" in the JSON response
    private String paymentStatus;

    @SerializedName("shipping_status") // Corresponds to "payment_status" in the JSON response
    private String shipping_status;

    @SerializedName("contact_id") // Corresponds to "payment_status" in the JSON response
    private int contact_id;

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    @SerializedName("contact") // This maps to the "contact" object in the JSON response
    private Contact contact;

    @SerializedName("invoice_url") // This maps to the "contact" object in the JSON response
    private String invoiceurl;

    @SerializedName("payment_link")
    private String paymenturl;


    @SerializedName("shipping_charges")
    private String shipping_charges;

    @SerializedName("location_name")
    private String location_name;

    public String getAdditional_notes() {
        return additional_notes;
    }

    public void setAdditional_notes(String additional_notes) {
        this.additional_notes = additional_notes;
    }

    @SerializedName("additional_notes")
    private String additional_notes;

    @SerializedName("pathao_delivery_status")
    private String pathao_delivery_status;

    public String getPathao_delivery_status() {
        return pathao_delivery_status;
    }

    public void setPathao_delivery_status(String pathao_delivery_status) {
        this.pathao_delivery_status = pathao_delivery_status;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getShipping_charges() {
        return shipping_charges;
    }

    public void setShipping_charges(String shipping_charges) {
        this.shipping_charges = shipping_charges;
    }

    @SerializedName("payment_lines")
    private List<PaymentLineModel> payment_lines;

    @SerializedName("location")
    private List<LocationResponse.Location> location;

    @SerializedName("sell_lines")
    private List<SellLineModel> sell_lines;

    public List<SellLineModel> getSell_lines() {
        return sell_lines;
    }

    public List<LocationResponse.Location> getLocation() {
        return location;
    }

    public void setLocation(List<LocationResponse.Location> location) {
        this.location = location;
    }

    public void setSell_lines(List<SellLineModel> sell_lines) {
        this.sell_lines = sell_lines;
    }

    public List<PaymentLineModel> getPayment_lines() {
        return payment_lines;
    }

    public void setPayment_lines(List<PaymentLineModel> payment_lines) {
        this.payment_lines = payment_lines;
    }

    public String getPaymenturl() {
        return paymenturl;
    }

    public void setPaymenturl(String paymenturl) {
        this.paymenturl = paymenturl;
    }

    public String getInvoiceurl() {
        return invoiceurl;
    }

    public void setInvoiceurl(String invoiceurl) {
        this.invoiceurl = invoiceurl;
    }



    // Other fields can be added as needed from the JSON response...

    // Getter and setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getFinal_total() {
        return final_total;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTotalBeforeTax() {
        return totalBeforeTax;
    }

    public void setTotalBeforeTax(String totalBeforeTax) {
        this.totalBeforeTax = totalBeforeTax;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getShipping_status() {
        return shipping_status;
    }

    public void setShipping_status(String shipping_status) {
        this.shipping_status = shipping_status;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // Optionally override toString() to log Sell object details
    @Override
    public String toString() {
        return "Sell{" +
                "id=" + id +
                ", invoiceNo='" + invoiceNo + '\'' +
                ", transactionDate='" + transactionDate + '\'' +
                ", totalBeforeTax='" + totalBeforeTax + '\'' +
                ", taxAmount='" + taxAmount + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", invoiceurl='" + invoiceurl + '\'' +
                ", paymenturl='" + paymenturl + '\'' +
                ", location_id='" + location_id + '\'' +
                ", shipping_charges='" + shipping_charges + '\'' +
                '}';

    }
}