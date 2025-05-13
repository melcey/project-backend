package com.cs308.backend.dto;

public class CreateCategoryRequest {
    private String name;
    private String description;

    public CreateCategoryRequest() {}
    
    public CreateCategoryRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CreateCategoryRequest [name=").append(name)
            .append(", description=").append(description)
            .append("]");

        return builder.toString();
    }
}
