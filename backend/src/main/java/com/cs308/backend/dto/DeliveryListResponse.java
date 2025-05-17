package com.cs308.backend.dto;

import java.util.ArrayList;
import java.util.List;

public class DeliveryListResponse {
    List<DeliveryResponse> deliveries;

    public DeliveryListResponse() {
        this.deliveries = new ArrayList<>();
    }

    public DeliveryListResponse(List<DeliveryResponse> deliveries) {
        this.deliveries = deliveries;
    }

    public List<DeliveryResponse> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<DeliveryResponse> deliveries) {
        this.deliveries = deliveries;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DeliveryListResponse [deliveries=").append(deliveries)
            .append("]");
            
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((deliveries == null) ? 0 : deliveries.hashCode());
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
        DeliveryListResponse other = (DeliveryListResponse) obj;
        if (deliveries == null) {
            if (other.deliveries != null)
                return false;
        } else if (!deliveries.equals(other.deliveries))
            return false;
        return true;
    }
}
