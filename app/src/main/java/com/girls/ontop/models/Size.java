package com.girls.ontop.models;

public class Size {
    private String createdAt;
    private int id;
    private String name;
    private String updatedAt;
    private int variationTemplateId;

    // No-argument constructor
    public Size() {
    }

    // All-argument constructor
    public Size(String createdAt, int id, String name, String updatedAt, int variationTemplateId) {
        this.createdAt = createdAt;
        this.id = id;
        this.name = name;
        this.updatedAt = updatedAt;
        this.variationTemplateId = variationTemplateId;
    }

    // Getters and Setters
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getVariationTemplateId() {
        return variationTemplateId;
    }

    public void setVariationTemplateId(int variationTemplateId) {
        this.variationTemplateId = variationTemplateId;
    }
}