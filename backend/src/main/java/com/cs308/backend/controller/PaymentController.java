package com.cs308.backend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cs308.backend.dao.CreditCard;
import com.cs308.backend.dao.Order;
import com.cs308.backend.dao.OrderItem;
import com.cs308.backend.dao.Payment;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.PaymentRequest;
import com.cs308.backend.dto.PaymentResponse;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.PaymentService;
import com.cs308.backend.service.ProductService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ProductService productService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest paymentRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.customer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Payment> payment = paymentService.processPayment(paymentRequest, user);

        if (!payment.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment processing failed");
        }

        Payment processedPayment = payment.get();

        Order order = processedPayment.getOrder();
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            int newStock = product.getQuantityInStock() - item.getQuantity();

            if (newStock < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product stock cannot be negative");
            }

            Optional<Product> updatedProduct = productService.updateProductQuantityInStock(
                    product.getId(),
                    newStock);

            if (!updatedProduct.isPresent()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Failed to update stock for product: " + product.getName());
            }
        }

        PaymentResponse response = new PaymentResponse(
                processedPayment.getId(),
                processedPayment.getOrder().getId(),
                processedPayment.getAmount(),
                processedPayment.getPaymentDate(),
                processedPayment.getPaymentStatus());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<PaymentResponse> getPaymentStatus(@PathVariable Long orderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.customer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Payment> payment = paymentService.findByOrderId(orderId);

        if (!(payment.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment could not be found");
        }

        Payment foundPayment = payment.get();

        if (!(foundPayment.getOrder().getUser().equals(user))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Payment does not belong to the user");
        }

        PaymentResponse response = new PaymentResponse(
                foundPayment.getId(),
                foundPayment.getOrder().getId(),
                foundPayment.getAmount(),
                foundPayment.getPaymentDate(),
                foundPayment.getPaymentStatus());

        return ResponseEntity.ok(response);
    }
}