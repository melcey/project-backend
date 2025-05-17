package com.cs308.backend.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cs308.backend.dao.Delivery;
import com.cs308.backend.dao.Order;
import com.cs308.backend.dao.Product;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByDeliveryStatus(String status);

    Optional<Delivery> findByOrderAndProduct(Order order, Product product);
}