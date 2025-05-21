package com.cs308.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cs308.backend.dao.Delivery;
import com.cs308.backend.dao.Order;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.DeliveryRepository;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public Optional<Delivery> createDelivery(Delivery delivery) {
        try {
            Delivery createdDelivery = deliveryRepository.save(delivery);
            return Optional.of(createdDelivery);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Delivery> getAllDeliveriesForProductManager(User productManager) {
        List<Delivery> deliveries = deliveryRepository.findAll();
        List<Delivery> deliveriesForProdMan = new ArrayList<>();
        
        for (Delivery delivery: deliveries) {
            if (delivery.getProduct().getProductManager().equals(productManager)) {
                deliveriesForProdMan.add(delivery);
            }
        }

        return deliveriesForProdMan;
    }

    public List<Delivery> getPendingDeliveriesForProductManager(User productManager) {
        List<Delivery> pendingDeliveries = deliveryRepository.findByDeliveryStatus("processing");
        List<Delivery> pendingDeliveriesForProdMan = new ArrayList<>();
        
        for (Delivery delivery: pendingDeliveries) {
            if (delivery.getProduct().getProductManager().equals(productManager)) {
                pendingDeliveriesForProdMan.add(delivery);
            }
        }

        return pendingDeliveriesForProdMan;
    }

    public Optional<Delivery> updateDeliveryStatus(Long deliveryId, String newStatus, User productManager) {
        try {
            Delivery delivery = deliveryRepository.findById(deliveryId)
                    .orElseThrow(() -> new RuntimeException("Delivery not found"));
            
            delivery.setDeliveryStatus(newStatus);
            Delivery updatedDelivery = deliveryRepository.save(delivery);
            return Optional.of(updatedDelivery);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Delivery> updateDeliveryStatusProductManager(Long deliveryId, String newStatus, User productManager) {
        try {
            Delivery delivery = deliveryRepository.findById(deliveryId)
                    .orElseThrow(() -> new RuntimeException("Delivery not found"));

            if (!(delivery.getProduct().getProductManager().equals(productManager))) {
                return Optional.empty();
            }

            delivery.setDeliveryStatus(newStatus);
            Delivery updatedDelivery = deliveryRepository.save(delivery);
            return Optional.of(updatedDelivery);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Delivery> findByOrderAndProduct(Order order, Product product) {
        return deliveryRepository.findByOrderAndProduct(order, product);
    }

    public Optional<Delivery> findById(Long id) {
        return deliveryRepository.findById(id);
    }
}
