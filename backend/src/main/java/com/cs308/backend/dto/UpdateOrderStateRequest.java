package com.cs308.backend.dto;

public class UpdateOrderStateRequest {
    private String status;

    public UpdateOrderStateRequest() {
        this.status = null;
    }

    public UpdateOrderStateRequest(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
