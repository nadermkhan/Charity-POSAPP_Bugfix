package com.girls.ontop.models;

import com.google.gson.annotations.SerializedName;

public class Expense {

    public Expense(int id, int businessId, int locationId, String paymentStatus, String refNo, String transactionDate, String totalBeforeTax, String taxId, String taxAmount, String finalTotal, String expenseCategoryId, String document, int createdBy, int isRecurring, String recurInterval, String recurIntervalType, String recurRepetitions, String recurStoppedOn, String recurParentId, String createdAt, String updatedAt, ExpenseUser transactionFor) {
        this.id = id;
        this.businessId = businessId;
        this.locationId = locationId;
        this.paymentStatus = paymentStatus;
        this.refNo = refNo;
        this.transactionDate = transactionDate;
        this.totalBeforeTax = totalBeforeTax;
        this.taxId = taxId;
        this.taxAmount = taxAmount;
        this.finalTotal = finalTotal;
        this.expenseCategoryId = expenseCategoryId;
        this.document = document;
        this.createdBy = createdBy;
        this.isRecurring = isRecurring;
        this.recurInterval = recurInterval;
        this.recurIntervalType = recurIntervalType;
        this.recurRepetitions = recurRepetitions;
        this.recurStoppedOn = recurStoppedOn;
        this.recurParentId = recurParentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.transactionFor = transactionFor;
    }

    @SerializedName("id")
    private int id;

    @SerializedName("business_id")
    private int businessId;

    @SerializedName("location_id")
    private int locationId;

    @SerializedName("payment_status")
    private String paymentStatus;

    @SerializedName("ref_no")
    private String refNo;

    @SerializedName("transaction_date")
    private String transactionDate;

    @SerializedName("total_before_tax")
    private String totalBeforeTax;

    @SerializedName("tax_id")
    private String taxId;

    @SerializedName("tax_amount")
    private String taxAmount;

    @SerializedName("final_total")
    private String finalTotal;

    @SerializedName("expense_category_id")
    private String expenseCategoryId;

    @SerializedName("document")
    private String document;

    @SerializedName("created_by")
    private int createdBy;

    @SerializedName("is_recurring")
    private int isRecurring;

    @SerializedName("recur_interval")
    private String recurInterval;

    @SerializedName("recur_interval_type")
    private String recurIntervalType;

    @SerializedName("recur_repetitions")
    private String recurRepetitions;

    @SerializedName("recur_stopped_on")
    private String recurStoppedOn;

    @SerializedName("recur_parent_id")
    private String recurParentId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("transaction_for")
    private ExpenseUser transactionFor;  // Nested object

    // Getters and Setters for each field

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getTransactionDate() {
        return transactionDate;
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

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getFinalTotal() {
        return finalTotal;
    }

    public void setFinalTotal(String finalTotal) {
        this.finalTotal = finalTotal;
    }

    public String getExpenseCategoryId() {
        return expenseCategoryId;
    }

    public void setExpenseCategoryId(String expenseCategoryId) {
        this.expenseCategoryId = expenseCategoryId;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(int isRecurring) {
        this.isRecurring = isRecurring;
    }

    public String getRecurInterval() {
        return recurInterval;
    }

    public void setRecurInterval(String recurInterval) {
        this.recurInterval = recurInterval;
    }

    public String getRecurIntervalType() {
        return recurIntervalType;
    }

    public void setRecurIntervalType(String recurIntervalType) {
        this.recurIntervalType = recurIntervalType;
    }

    public String getRecurRepetitions() {
        return recurRepetitions;
    }

    public void setRecurRepetitions(String recurRepetitions) {
        this.recurRepetitions = recurRepetitions;
    }

    public String getRecurStoppedOn() {
        return recurStoppedOn;
    }

    public void setRecurStoppedOn(String recurStoppedOn) {
        this.recurStoppedOn = recurStoppedOn;
    }

    public String getRecurParentId() {
        return recurParentId;
    }

    public void setRecurParentId(String recurParentId) {
        this.recurParentId = recurParentId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ExpenseUser getTransactionFor() {
        return transactionFor;
    }

    public void setTransactionFor(ExpenseUser transactionFor) {
        this.transactionFor = transactionFor;
    }
}