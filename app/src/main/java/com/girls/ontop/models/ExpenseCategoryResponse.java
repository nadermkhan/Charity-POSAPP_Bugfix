package com.girls.ontop.models;

import java.util.List;

public class ExpenseCategoryResponse {
    private List<ExpenseCategory> data;


    // Getter and Setter
    public List<ExpenseCategory> getData() { return data; }
    public void setData(List<ExpenseCategory> data) { this.data = data; }

    public class ExpenseCategory {
        private int id;
        private String name;
        private int businessId;
        private String code;
        private Integer parentId;
        private String deletedAt;
        private String createdAt;
        private String updatedAt;
        private List<ExpenseSubCategory> sub_categories;

        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getBusinessId() { return businessId; }
        public void setBusinessId(int businessId) { this.businessId = businessId; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public Integer getParentId() { return parentId; }
        public void setParentId(Integer parentId) { this.parentId = parentId; }
        public String getDeletedAt() { return deletedAt; }
        public void setDeletedAt(String deletedAt) { this.deletedAt = deletedAt; }
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
        public String getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
        public List<ExpenseSubCategory> getSubCategories() { return sub_categories; }
        public void setSubCategories(List<ExpenseSubCategory> subCategories) { this.sub_categories = subCategories; }

        public ExpenseCategory(int id, String name, int businessId, String code, Integer parentId, String deletedAt, String createdAt, String updatedAt, List<ExpenseSubCategory> subCategories) {
            this.id = id;
            this.name = name;
            this.businessId = businessId;
            this.code = code;
            this.parentId = parentId;
            this.deletedAt = deletedAt;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.sub_categories = subCategories;
        }
    }

    public class ExpenseSubCategory {
        private int id;
        private String name;
        private int businessId;
        private String code;
        private Integer parentId;
        private String deletedAt;
        private String createdAt;
        private String updatedAt;

        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getBusinessId() { return businessId; }
        public void setBusinessId(int businessId) { this.businessId = businessId; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public Integer getParentId() { return parentId; }
        public void setParentId(Integer parentId) { this.parentId = parentId; }
        public String getDeletedAt() { return deletedAt; }
        public void setDeletedAt(String deletedAt) { this.deletedAt = deletedAt; }
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
        public String getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    }
}




