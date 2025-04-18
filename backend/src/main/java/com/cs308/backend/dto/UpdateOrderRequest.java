package com.cs308.backend.dto;

public class UpdateOrderRequest {
    private String status;

    public UpdateOrderRequest() {
        status = null;
    }

    public UpdateOrderRequest(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
