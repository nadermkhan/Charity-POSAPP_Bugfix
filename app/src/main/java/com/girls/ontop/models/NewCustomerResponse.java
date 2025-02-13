package com.girls.ontop.models;

public class NewCustomerResponse {

    private Data data; // Single customer object, not a list

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String type;
        private String supplier_business_name;
        private String first_name;
        private String mobile;
        private String address_line_1;  // Changed from int to String
        private String shipping_address; // Changed from int to String
        private String business_id;
        private String created_by;
        private String credit_limit;
        private String name;
        private String contact_id;
        private String updated_at;
        private String created_at;
        private int id;

        public Data(String type, String supplier_business_name, String first_name, String mobile, String address_line_1, String shipping_address, String business_id, String created_by, String credit_limit, String name, String contact_id, String updated_at, String created_at, int id) {
            this.type = type;
            this.supplier_business_name = supplier_business_name;
            this.first_name = first_name;
            this.mobile = mobile;
            this.address_line_1 = address_line_1;
            this.shipping_address = shipping_address;
            this.business_id = business_id;
            this.created_by = created_by;
            this.credit_limit = credit_limit;
            this.name = name;
            this.contact_id = contact_id;
            this.updated_at = updated_at;
            this.created_at = created_at;
            this.id = id;
        }

        // Getters and setters for all fields...
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSupplier_business_name() {
            return supplier_business_name;
        }

        public void setSupplier_business_name(String supplier_business_name) {
            this.supplier_business_name = supplier_business_name;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAddress_line_1() {
            return address_line_1;
        }

        public void setAddress_line_1(String address_line_1) { // Changed type to String
            this.address_line_1 = address_line_1;
        }

        public String getShipping_address() {
            return shipping_address;
        }

        public void setShipping_address(String shipping_address) { // Changed type to String
            this.shipping_address = shipping_address;
        }

        public String getBusiness_id() {
            return business_id;
        }

        public void setBusiness_id(String business_id) {
            this.business_id = business_id;
        }

        public String getCreated_by() {
            return created_by;
        }

        public void setCreated_by(String created_by) {
            this.created_by = created_by;
        }

        public String getCredit_limit() {
            return credit_limit;
        }

        public void setCredit_limit(String credit_limit) {
            this.credit_limit = credit_limit;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContact_id() {
            return contact_id;
        }

        public void setContact_id(String contact_id) {
            this.contact_id = contact_id;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
