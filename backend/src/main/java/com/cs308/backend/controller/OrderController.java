package com.cs308.backend.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cs308.backend.dao.Invoice;
import com.cs308.backend.dao.Order;
import com.cs308.backend.dao.OrderItem;
import com.cs308.backend.dao.OrderStatus;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.CategoryResponse;
import com.cs308.backend.dto.CreateOrderRequest;
import com.cs308.backend.dto.InvoiceResponse;
import com.cs308.backend.dto.OrderItemRequest;
import com.cs308.backend.dto.OrderItemResponse;
import com.cs308.backend.dto.OrderResponse;
import com.cs308.backend.dto.ProductResponse;
import com.cs308.backend.dto.UpdateOrderStateRequest;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.InvoiceService;
import com.cs308.backend.service.OrderService;
import com.cs308.backend.service.ProductService;


@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final ProductService productService;
    private final InvoiceService invoiceService;

    public OrderController(OrderService orderService, ProductService productService, InvoiceService invoiceService) {
        this.orderService = orderService;
        this.productService = productService;
        this.invoiceService = invoiceService;
    }

    @GetMapping("/customer")
    public ResponseEntity<?> getAllOrdersByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.customer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        List<Order> ordersByUser = orderService.findAllOrdersByUser(user);

        List<OrderItemResponse> orderItems = new ArrayList<>();
        List<OrderResponse> response = new ArrayList<>();

        for (Order orderByUser: ordersByUser) {
            for (OrderItem orderItemByUser: orderByUser.getOrderItems()) {
                orderItems.add(new OrderItemResponse(orderItemByUser.getId(), orderByUser.getId(), new ProductResponse(orderItemByUser.getProduct().getId(), orderItemByUser.getProduct().getName(), orderItemByUser.getProduct().getModel(), orderItemByUser.getProduct().getSerialNumber(), orderItemByUser.getProduct().getDescription(), orderItemByUser.getProduct().getQuantityInStock(), orderItemByUser.getProduct().getPrice(), orderItemByUser.getProduct().getWarrantyStatus(), orderItemByUser.getProduct().getDistributorInfo(), orderItemByUser.getProduct().getIsActive(), orderItemByUser.getProduct().getImageUrl(), new CategoryResponse(orderItemByUser.getProduct().getCategory().getId(), orderItemByUser.getProduct().getCategory().getName(), orderItemByUser.getProduct().getCategory().getDescription())), orderItemByUser.getQuantity(), orderItemByUser.getPrice()));
            }

            response.add(new OrderResponse(orderByUser.getId(), user.getId(), LocalDateTime.now(), orderByUser.getStatus(), orderByUser.getTotalPrice(), orderByUser.getDeliveryAddress(), orderItems));
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<?> getOrderByUser(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.customer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Order> retrievedOrder = orderService.findOrder(id);

        if (!(retrievedOrder.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order could not be found");
        }

        if (!(user.equals(retrievedOrder.get().getUser()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Order does not belong to the user");
        }

        List<OrderItemResponse> orderItems = new ArrayList<>();

        for (OrderItem orderItem: retrievedOrder.get().getOrderItems()) {
            orderItems.add(new OrderItemResponse(orderItem.getId(), retrievedOrder.get().getId(), new ProductResponse(orderItem.getProduct().getId(), orderItem.getProduct().getName(), orderItem.getProduct().getModel(), orderItem.getProduct().getSerialNumber(), orderItem.getProduct().getDescription(), orderItem.getProduct().getQuantityInStock(), orderItem.getProduct().getPrice(), orderItem.getProduct().getWarrantyStatus(), orderItem.getProduct().getDistributorInfo(), orderItem.getProduct().getIsActive(), orderItem.getProduct().getImageUrl(), new CategoryResponse(orderItem.getProduct().getCategory().getId(), orderItem.getProduct().getCategory().getName(), orderItem.getProduct().getCategory().getDescription())), orderItem.getQuantity(), orderItem.getPrice()));
        }

        return ResponseEntity.ok(new OrderResponse(retrievedOrder.get().getId(), user.getId(), retrievedOrder.get().getOrderDate(), retrievedOrder.get().getStatus(), retrievedOrder.get().getTotalPrice(), retrievedOrder.get().getDeliveryAddress(), orderItems));
    }

    @GetMapping("/customer/{id}/invoice")
    public ResponseEntity<?> getInvoiceForOrderByUser(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.customer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Order> retrievedOrder = orderService.findOrder(id);

        if (!(retrievedOrder.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order could not be found");
        }

        if (!(user.equals(retrievedOrder.get().getUser()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Order does not belong to the user");
        }

        Optional<Invoice> retrievedInvoice = invoiceService.findByOrder(retrievedOrder.get());

        if (!(retrievedInvoice.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice could not be found");
        }

        Invoice invoice = retrievedInvoice.get();

        return ResponseEntity.ok(new InvoiceResponse(invoice.getId(), invoice.getInvoiceNumber(), invoice.getOrder().getId(), invoice.getPayment().getId(), invoice.getInvoiceDate(), invoice.getTotalAmount()));
    }
    
    @PostMapping("/customer")
    public ResponseEntity<?> createNewOrder(@RequestBody CreateOrderRequest orderToCreate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.customer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest newOrderItem: orderToCreate.getOrderItems()) {
            Optional<Product> currentProduct = productService.findProductById(newOrderItem.getProductId());
            if (!(currentProduct.isPresent())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product could not be found");
            }

            orderItems.add(new OrderItem(null, currentProduct.get(), newOrderItem.getQuantity(), newOrderItem.getPrice()));
        }
        
        try {
            Order newOrder = new Order(user, OrderStatus.fromString(orderToCreate.getStatus()), orderToCreate.getTotalPrice(), user.getAddress(), null);
            
            for (OrderItem orderItem: orderItems) {
                orderItem.setOrder(newOrder);
            }
    
            newOrder.setOrderItems(orderItems);
    
            Optional<Order> addedOrder = orderService.addNewOrder(newOrder);
    
            if (!(addedOrder.isPresent())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order creation failed");
            }
    
            List<OrderItemResponse> responseItems = new ArrayList<>();
    
            for (OrderItem addedOrderItem: addedOrder.get().getOrderItems()) {
                responseItems.add(new OrderItemResponse(addedOrderItem.getId(), addedOrder.get().getId(), new ProductResponse(addedOrderItem.getProduct().getId(), addedOrderItem.getProduct().getName(), addedOrderItem.getProduct().getModel(), addedOrderItem.getProduct().getSerialNumber(), addedOrderItem.getProduct().getDescription(), addedOrderItem.getProduct().getQuantityInStock(), addedOrderItem.getProduct().getPrice(), addedOrderItem.getProduct().getWarrantyStatus(), addedOrderItem.getProduct().getDistributorInfo(), addedOrderItem.getProduct().getIsActive(), addedOrderItem.getProduct().getImageUrl(), new CategoryResponse(addedOrderItem.getProduct().getCategory().getId(), addedOrderItem.getProduct().getCategory().getName(), addedOrderItem.getProduct().getCategory().getDescription())), addedOrderItem.getQuantity(), addedOrderItem.getPrice()));
            }
    
            return ResponseEntity.ok(new OrderResponse(addedOrder.get().getId(), user.getId(), addedOrder.get().getOrderDate(), addedOrder.get().getStatus(), addedOrder.get().getTotalPrice(), addedOrder.get().getDeliveryAddress(), responseItems));
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order creation failed");
        }
    }

    @GetMapping("/manager/{id}")
    public ResponseEntity<?> getOrderForProductManager(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Order> retrievedOrder = orderService.findOrder(id);

        if (!(retrievedOrder.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order could not be found");
        }

        boolean hasManagedProducts = retrievedOrder.get().getOrderItems().stream()
            .anyMatch(orderItem -> orderItem.getProduct().getProductManager().equals(user));

        if (!hasManagedProducts) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No products in the associated order belong to the product manager");
        }

        List<OrderItemResponse> responseItems = new ArrayList<>();

        for (OrderItem retrievedOrderItem: retrievedOrder.get().getOrderItems()) {
            responseItems.add(new OrderItemResponse(retrievedOrderItem.getId(), retrievedOrder.get().getId(), new ProductResponse(retrievedOrderItem.getProduct().getId(), retrievedOrderItem.getProduct().getName(), retrievedOrderItem.getProduct().getModel(), retrievedOrderItem.getProduct().getSerialNumber(), retrievedOrderItem.getProduct().getDescription(), retrievedOrderItem.getProduct().getQuantityInStock(), retrievedOrderItem.getProduct().getPrice(), retrievedOrderItem.getProduct().getWarrantyStatus(), retrievedOrderItem.getProduct().getDistributorInfo(), retrievedOrderItem.getProduct().getIsActive(), retrievedOrderItem.getProduct().getImageUrl(), new CategoryResponse(retrievedOrderItem.getProduct().getCategory().getId(), retrievedOrderItem.getProduct().getCategory().getName(), retrievedOrderItem.getProduct().getCategory().getDescription())), retrievedOrderItem.getQuantity(), retrievedOrderItem.getPrice()));
        }

        return ResponseEntity.ok(new OrderResponse(retrievedOrder.get().getId(), user.getId(), retrievedOrder.get().getOrderDate(), retrievedOrder.get().getStatus(), retrievedOrder.get().getTotalPrice(), retrievedOrder.get().getDeliveryAddress(), responseItems));
    }

    @GetMapping("/manager/{id}/invoice")
    public ResponseEntity<?> getInvoiceForOrderForProductManager(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Order> retrievedOrder = orderService.findOrder(id);

        if (!(retrievedOrder.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order could not be found");
        }

        boolean hasManagedProducts = retrievedOrder.get().getOrderItems().stream()
            .anyMatch(orderItem -> orderItem.getProduct().getProductManager().equals(user));

        if (!hasManagedProducts) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No products in the associated order belong to the product manager");
        }

        Optional<Invoice> retrievedInvoice = invoiceService.findByOrder(retrievedOrder.get());

        if (!(retrievedInvoice.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice could not be found");
        }

        Invoice invoice = retrievedInvoice.get();

        return ResponseEntity.ok(new InvoiceResponse(invoice.getId(), invoice.getInvoiceNumber(), invoice.getOrder().getId(), invoice.getPayment().getId(), invoice.getInvoiceDate(), invoice.getTotalAmount()));
    }
    
    // One of "pending", in-transit", "delivered"
    @PutMapping("/manager/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody UpdateOrderStateRequest newStateRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Order> retrievedOrder = orderService.findOrder(id);

        if (!(retrievedOrder.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order could not be found");
        }
        
        Optional<Order> updatedOrder = orderService.updateOrderStatus(retrievedOrder.get(), newStateRequest.getStatus());

        if (!(updatedOrder.isPresent())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order status update failed");
        }

        List<OrderItemResponse> responseItems = new ArrayList<>();

        for (OrderItem updatedOrderItem: updatedOrder.get().getOrderItems()) {
            responseItems.add(new OrderItemResponse(updatedOrderItem.getId(), updatedOrder.get().getId(), new ProductResponse(updatedOrderItem.getProduct().getId(), updatedOrderItem.getProduct().getName(), updatedOrderItem.getProduct().getModel(), updatedOrderItem.getProduct().getSerialNumber(), updatedOrderItem.getProduct().getDescription(), updatedOrderItem.getProduct().getQuantityInStock(), updatedOrderItem.getProduct().getPrice(), updatedOrderItem.getProduct().getWarrantyStatus(), updatedOrderItem.getProduct().getDistributorInfo(), updatedOrderItem.getProduct().getIsActive(), updatedOrderItem.getProduct().getImageUrl(), new CategoryResponse(updatedOrderItem.getProduct().getCategory().getId(), updatedOrderItem.getProduct().getCategory().getName(), updatedOrderItem.getProduct().getCategory().getDescription())), updatedOrderItem.getQuantity(), updatedOrderItem.getPrice()));
        }

        return ResponseEntity.ok(new OrderResponse(updatedOrder.get().getId(), user.getId(), updatedOrder.get().getOrderDate(), updatedOrder.get().getStatus(), updatedOrder.get().getTotalPrice(), updatedOrder.get().getDeliveryAddress(), responseItems));
    }

    // To be unused right now
    /*@DeleteMapping("/customer/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.customer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Order> retrievedOrder = orderService.findOrder(id);

        if (!(retrievedOrder.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order could not be found");
        }

        Order orderToDelete = retrievedOrder.get();

        if (!(user.equals(orderToDelete.getUser()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Order does not belong to the user");
        }

        Optional<Order> deletedOrder = orderService.updateOrderStatus(orderToDelete, "cancelled");

        if (!(deletedOrder.isPresent())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order deletion failed");
        }

        List<OrderItemResponse> responseItems = new ArrayList<>();

        for (OrderItem deletedOrderItem: deletedOrder.get().getOrderItems()) {
            responseItems.add(new OrderItemResponse(deletedOrderItem.getId(), deletedOrder.get().getId(), new ProductResponse(deletedOrderItem.getProduct().getId(), deletedOrderItem.getProduct().getName(), deletedOrderItem.getProduct().getModel(), deletedOrderItem.getProduct().getSerialNumber(), deletedOrderItem.getProduct().getDescription(), deletedOrderItem.getProduct().getQuantityInStock(), deletedOrderItem.getProduct().getPrice(), deletedOrderItem.getProduct().getWarrantyStatus(), deletedOrderItem.getProduct().getDistributorInfo(), deletedOrderItem.getProduct().getIsActive(), deletedOrderItem.getProduct().getImageUrl(), new CategoryResponse(deletedOrderItem.getProduct().getCategory().getId(), deletedOrderItem.getProduct().getCategory().getName(), deletedOrderItem.getProduct().getCategory().getDescription())), deletedOrderItem.getQuantity(), deletedOrderItem.getPrice()));
        }

        return ResponseEntity.ok(new OrderResponse(deletedOrder.get().getId(), user.getId(), deletedOrder.get().getOrderDate(), deletedOrder.get().getStatus(), deletedOrder.get().getTotalPrice(), deletedOrder.get().getDeliveryAddress(), responseItems));
    }*/
}
