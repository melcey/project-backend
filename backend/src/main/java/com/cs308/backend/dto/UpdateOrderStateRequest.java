package com.cs308.backend.dto;

public class UpdateOrderStateRequest {
    private String newStatus;

    public UpdateOrderStateRequest() {
    }

    public UpdateOrderStateRequest(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UpdateOrderStateRequest [newStatus=").append(newStatus)
                .append("]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((newStatus == null) ? 0 : newStatus.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UpdateOrderStateRequest other = (UpdateOrderStateRequest) obj;
        if (newStatus == null) {
            if (other.newStatus != null)
                return false;
        } else if (!newStatus.equals(other.newStatus))
            return false;
        return true;
    }
}
