package com.girls.ontop.models;

import android.provider.ContactsContract;

import java.util.List;

public class RedXLocationStatus {

    private List<ContactsContract.Data> data;
    private String status;

    // Default constructor
    public RedXLocationStatus() {
    }

    // Constructor with parameters
    public RedXLocationStatus(List<ContactsContract.Data> data, String status) {
        this.data = data;
        this.status = status;
    }

    // Getter for data
    public List<ContactsContract.Data> getData() {
        return data;
    }

    // Setter for data
    public void setData(List<ContactsContract.Data> data) {
        this.data = data;
    }

    // Getter for status
    public String getStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(String status) {
        this.status = status;
    }
}