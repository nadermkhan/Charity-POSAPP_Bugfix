package com.girls.ontop.models;
import com.google.gson.annotations.SerializedName;
public class UserResponse {

    @SerializedName("data")
    private UserData data;

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    // Inner class to map user data
    public static class UserData {

        @SerializedName("id")
        private int id;

        @SerializedName("user_type")
        private String userType;

        @SerializedName("surname")
        private String surname;

        @SerializedName("first_name")
        private String firstName;

        @SerializedName("last_name")
        private String lastName;

        @SerializedName("username")
        private String username;

        @SerializedName("email")
        private String email;

        @SerializedName("language")
        private String language;

        @SerializedName("contact_no")
        private String contactNo;

        @SerializedName("address")
        private String address;

        @SerializedName("business_id")
        private int businessId;

        @SerializedName("max_sales_discount_percent")
        private String maxSalesDiscountPercent;

        @SerializedName("allow_login")
        private int allowLogin;

        @SerializedName("status")
        private String status;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("updated_at")
        private String updatedAt;

        // Add other fields as required...

        // Getters and Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getBusinessId() {
            return businessId;
        }

        public void setBusinessId(int businessId) {
            this.businessId = businessId;
        }

        public String getMaxSalesDiscountPercent() {
            return maxSalesDiscountPercent;
        }

        public void setMaxSalesDiscountPercent(String maxSalesDiscountPercent) {
            this.maxSalesDiscountPercent = maxSalesDiscountPercent;
        }

        public int getAllowLogin() {
            return allowLogin;
        }

        public void setAllowLogin(int allowLogin) {
            this.allowLogin = allowLogin;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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
    }
}

