package com.cs308.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cs308.backend.dao.Order;
import com.cs308.backend.dao.OrderItem;
import com.cs308.backend.dao.OrderStatus;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.OrderItemRepository;
import com.cs308.backend.repo.OrderRepository;
import com.cs308.backend.dao.Delivery;
import com.cs308.backend.repo.DeliveryRepository;
import com.cs308.backend.dao.Invoice;
import com.cs308.backend.repo.InvoiceRepository;
import com.cs308.backend.dao.UpdateOrderStateRequest;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final DeliveryRepository deliveryRepository;
    private final InvoiceRepository invoiceRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
            DeliveryRepository deliveryRepository, InvoiceRepository invoiceRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.deliveryRepository = deliveryRepository;
        this.invoiceRepository = invoiceRepository;
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

    public ResponseEntity<List<Delivery>> getAllDeliveries() {
        try {
            List<Delivery> deliveries = deliveryRepository.findAll();
            return ResponseEntity.ok(deliveries);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    public ResponseEntity<List<Delivery>> getPendingDeliveries() {
        try {
            List<Delivery> pendingDeliveries = deliveryRepository.findByDeliveryStatus("PENDING");
            return ResponseEntity.ok(pendingDeliveries);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    public ResponseEntity<?> updateDeliveryStatus(Long deliveryId, String newStatus) {
        try {
            Delivery delivery = deliveryRepository.findById(deliveryId)
                    .orElseThrow(() -> new RuntimeException("Delivery not found"));

            delivery.setDeliveryStatus(newStatus);
            Delivery updatedDelivery = deliveryRepository.save(delivery);
            return ResponseEntity.ok(updatedDelivery);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating delivery status: " + e.getMessage());
        }
    }

    public ResponseEntity<?> updateOrderStatus(Long orderId, UpdateOrderStateRequest request) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            order.setOrderStatus(request.getNewStatus());
            Order updatedOrder = orderRepository.save(order);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating order status: " + e.getMessage());
        }
    }

    public ResponseEntity<List<Invoice>> getAllInvoices() {
        try {
            List<Invoice> invoices = invoiceRepository.findAll();
            return ResponseEntity.ok(invoices);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    public ResponseEntity<Invoice> getInvoiceByOrderId(Long orderId) {
        try {
            Invoice invoice = invoiceRepository.findByOrderId(orderId)
                    .orElseThrow(() -> new RuntimeException("Invoice not found"));
            return ResponseEntity.ok(invoice);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
