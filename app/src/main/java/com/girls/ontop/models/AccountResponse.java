package com.girls.ontop.models;

import java.util.List;

public class AccountResponse {

    private List<Accounts> data;

    public List<Accounts> getData() {
        return data;
    }

    public void setData(List<Accounts> data) {
        this.data = data;
    }

    public static class Accounts {
        private int id;
        private String name;
        private String account_number;

        public Accounts(int id, String name, String account_number) {
            this.id = id;
            this.name = name;
            this.account_number = account_number;
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

        public String getAccount_number() {
            return account_number;
        }

        public void setAccount_number(String account_number) {
            this.account_number = account_number;
        }
    }
}
