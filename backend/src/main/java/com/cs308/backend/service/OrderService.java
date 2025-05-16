package com.cs308.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cs308.backend.dao.Order;
import com.cs308.backend.dao.OrderItem;
import com.cs308.backend.dao.OrderStatus;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.OrderItemRepository;
import com.cs308.backend.repo.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Optional<Order> addNewOrder(Order orderToAdd) {
        try {
            return Optional.of(orderRepository.save(orderToAdd));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Order> updateOrderStatus(Order orderToUpdate, String newStatus) {
        try {
            orderToUpdate.setStatus(OrderStatus.fromString(newStatus));
            return Optional.of(orderRepository.save(orderToUpdate));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Order> findAllOrdersByUser(User user) {
        return orderRepository.findAllByUser(user);
    }

    public Optional<Order> findOrder(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<OrderItem> findAllOrderItemsByProduct(Product product) {
        return orderItemRepository.findAllByProduct(product);
    }

    public List<Order> findAllOrdersIncludingProduct(Product product) {
        List<Order> ordersWithProduct = new ArrayList<>();

        List<OrderItem> orderItemsOfProduct = orderItemRepository.findAllByProduct(product);
        for (OrderItem orderItemOfProduct : orderItemsOfProduct) {
            Optional<Order> retrievedOrder = orderRepository.findByOrderItemsContains(orderItemOfProduct);

            if (retrievedOrder.isPresent()) {
                ordersWithProduct.add(retrievedOrder.get());
            }
        }

        return ordersWithProduct;
    }
}
