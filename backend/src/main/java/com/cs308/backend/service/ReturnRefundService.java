package com.cs308.backend.service;

import com.cs308.backend.dao.Order;
import com.cs308.backend.dao.OrderItem;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Refund;
import com.cs308.backend.dao.ReturnRequest;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.OrderRepository;
import com.cs308.backend.repo.ProductRepository;
import com.cs308.backend.repo.RefundRepository;
import com.cs308.backend.repo.ReturnRequestRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReturnRefundService {
    
    private final ReturnRequestRepository returnRequestRepository;
    private final RefundRepository refundRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final EmailService emailService;
    
    private static final int MAX_DAYS_FOR_RETURN = 30;
    
    public ReturnRefundService(ReturnRequestRepository returnRequestRepository,
                              RefundRepository refundRepository,
                              ProductRepository productRepository,
                              OrderRepository orderRepository,
                              EmailService emailService) {
        this.returnRequestRepository = returnRequestRepository;
        this.refundRepository = refundRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.emailService = emailService;
    }
    
    /**
     * Create a return request for a product
     * @param user the user making the request
     * @param orderId the order ID
     * @param productId the product ID
     * @param quantity quantity to return
     * @param reason reason for return
     * @return the created return request
     */
    public ReturnRequest createReturnRequest(User user, Long orderId, Long productId, int quantity, String reason) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Order not found"));
            
        // Check if order belongs to user
        if (!order.getUser().equals(user)) {
            throw new IllegalArgumentException("Order does not belong to this user");
        }
        
        // Check if order is within return window
        LocalDateTime orderDate = order.getOrderDate();
        LocalDateTime now = LocalDateTime.now();
        long daysBetween = ChronoUnit.DAYS.between(orderDate, now);
        
        if (daysBetween > MAX_DAYS_FOR_RETURN) {
            throw new IllegalArgumentException("Return period has expired (30 days)");
        }
        
        // Check if order has been delivered
        if (!order.getStatus().equals("DELIVERED")) {
            throw new IllegalArgumentException("Order has not been delivered yet");
        }
        
        // Find the order item containing the product
        Optional<OrderItem> orderItemOpt = order.getOrderItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst();
            
        if (!orderItemOpt.isPresent()) {
            throw new EntityNotFoundException("Product not found in order");
        }
        
        OrderItem orderItem = orderItemOpt.get();
        
        // Check if quantity is valid
        if (quantity <= 0 || quantity > orderItem.getQuantity()) {
            throw new IllegalArgumentException("Invalid return quantity");
        }
        
        // Create the return request
        ReturnRequest returnRequest = new ReturnRequest(
            order,
            user,
            orderItem.getProduct(),
            quantity,
            orderItem.getPrice(), // Price at time of purchase (including any discount)
            reason
        );
        
        return returnRequestRepository.save(returnRequest);
    }
    
    /**
     * Get all pending return requests
     * @return list of pending requests
     */
    public List<ReturnRequest> getPendingReturnRequests() {
        return returnRequestRepository.findAllPendingRequests();
    }
    
    /**
     * Get return requests for a user
     * @param user the user
     * @return list of return requests
     */
    public List<ReturnRequest> getUserReturnRequests(User user) {
        return returnRequestRepository.findByUser(user);
    }
    
    /**
     * Process a return request
     * @param requestId ID of the return request
     * @param salesManager the sales manager processing the request
     * @param approved whether the request is approved
     * @return the processed refund (if approved)
     */
    public Refund processReturnRequest(Long requestId, User salesManager, boolean approved) {
        ReturnRequest returnRequest = returnRequestRepository.findById(requestId)
            .orElseThrow(() -> new EntityNotFoundException("Return request not found"));
            
        if (!returnRequest.getStatus().equals(ReturnRequest.Status.PENDING)) {
            throw new IllegalStateException("Return request has already been processed");
        }
        
        returnRequest.setResolvedBy(salesManager);
        returnRequest.setResolutionDate(LocalDateTime.now());
        
        if (approved) {
            returnRequest.setStatus(ReturnRequest.Status.APPROVED);
            
            // Add product back to stock
            Product product = returnRequest.getProduct();
            product.setQuantityInStock(product.getQuantityInStock() + returnRequest.getQuantity());
            productRepository.save(product);
            
            // Create refund
            BigDecimal refundAmount = returnRequest.getOriginalPrice()
                .multiply(new BigDecimal(returnRequest.getQuantity()));
                
            Refund refund = new Refund(returnRequest, refundAmount);
            refund = refundRepository.save(refund);
            
            // Notify user
            sendRefundApprovalEmail(returnRequest, refundAmount);
            
            return refund;
        } else {
            returnRequest.setStatus(ReturnRequest.Status.REJECTED);
            returnRequestRepository.save(returnRequest);
            
            // Notify user
            sendRefundRejectionEmail(returnRequest);
            
            return null;
        }
    }
    
    /**
     * Complete a refund (mark as paid)
     * @param refundId ID of the refund
     */
    public Refund completeRefund(Long refundId) {
        Refund refund = refundRepository.findById(refundId)
            .orElseThrow(() -> new EntityNotFoundException("Refund not found"));
            
        refund.setStatus(Refund.Status.COMPLETED);
        return refundRepository.save(refund);
    }
    
    /**
     * Send email notification about approved refund
     */
    private void sendRefundApprovalEmail(ReturnRequest returnRequest, BigDecimal refundAmount) {
        User user = returnRequest.getUser();
        Product product = returnRequest.getProduct();
        
        String subject = "Your Return Request Has Been Approved";
        String text = String.format(
            "Dear %s,\n\nYour request to return %d units of %s has been approved. " +
            "A refund of $%s will be processed and credited back to your payment method. " +
            "Thank you for shopping with us!\n\nBest regards,\nThe Sales Team",
            user.getName(),
            returnRequest.getQuantity(),
            product.getName(),
            refundAmount.toString()
        );
        
        emailService.sendEmail(user.getEmail(), subject, text);
    }
    
    /**
     * Send email notification about rejected refund
     */
    private void sendRefundRejectionEmail(ReturnRequest returnRequest) {
        User user = returnRequest.getUser();
        Product product = returnRequest.getProduct();
        
        String subject = "Your Return Request Status";
        String text = String.format(
            "Dear %s,\n\nUnfortunately, your request to return %d units of %s could not be approved. " +
            "If you have any questions, please contact our customer service.\n\nBest regards,\nThe Sales Team",
            user.getName(),
            returnRequest.getQuantity(),
            product.getName()
        );
        
        emailService.sendEmail(user.getEmail(), subject, text);
    }
}