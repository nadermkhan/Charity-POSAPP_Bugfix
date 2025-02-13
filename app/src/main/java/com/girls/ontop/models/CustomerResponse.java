package com.girls.ontop.models;

import java.util.List;

public class CustomerResponse {

    private List<Customer> data;

    public List<Customer> getData() {
        return data;
    }

    public void setData(List<Customer> data) {
        this.data = data;
    }

    public static class Customer {
        private int id;
        private String name;
        private  String contact_id;
        private String address_line_1;
        private String mobile;

        public Customer(int id, String name, String contact_id, String address_line_1,String mobile) {
            this.id = id;
            this.name = name;
            this.contact_id = contact_id;
            this.address_line_1 = address_line_1;
            this.mobile = mobile;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getAddress_line_1() {
            return address_line_1;
        }

        public void setAddress_line_1(String address_line_1) {
            this.address_line_1 = address_line_1;
        }
    }
}
