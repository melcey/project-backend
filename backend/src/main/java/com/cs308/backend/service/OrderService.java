package com.cs308.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cs308.backend.dao.Order;
import com.cs308.backend.dao.OrderItem;
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

    public Order addNewOrder(Order orderToAdd) {
        return orderRepository.save(orderToAdd);
    }

    public Order updateOrderStatus(Order orderToUpdate, String newStatus) {
        orderToUpdate.setStatus(newStatus);
        return orderRepository.save(orderToUpdate);
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
        for (OrderItem orderItemOfProduct: orderItemsOfProduct) {
            if (orderRepository.findByOrderItemsContains(orderItemOfProduct).isPresent()) {
                ordersWithProduct.add(orderRepository.findByOrderItemsContains(orderItemOfProduct).get());
            }
        }

        return ordersWithProduct;
    }
}
