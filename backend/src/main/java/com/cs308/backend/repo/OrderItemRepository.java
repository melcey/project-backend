package com.cs308.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cs308.backend.dao.Order;
import com.cs308.backend.dao.OrderItem;
import com.cs308.backend.dao.Product;
import java.util.List;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByProduct(Product product);
    List<OrderItem> findByOrder(Order order);
}
